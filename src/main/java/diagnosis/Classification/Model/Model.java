package diagnosis.Classification.Model;

import diagnosis.Classification.Helpers.TrainingSession;
import diagnosis.Utilities.UILogger;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.ImageTransform;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.MultipleEpochsIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.deeplearning4j.util.NetSaverLoaderUtils;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract public class Model extends Layer {

    public static final int MODEL_ALEXNET = 1;
    public static final int MODEL_LENET   = 2;
    public static final int MODEL_CUSTOM  = 3;

    protected MultiLayerNetwork network;

    protected int height = 100;
    protected int width = 100;
    protected int channels = 3;

    protected long seed = 42;
    protected Random rng = new Random(seed);
    protected int listenerFreq = 1;
    protected double splitTrainTest = 0.8;

    protected boolean alreadyTrained = false;
    protected TrainingSession session;
    protected UILogger logger;

    public Model(TrainingSession session) {
        this.session = session;
    }

    abstract public MultiLayerNetwork getModel();

    public void run() throws Exception {
        this.logger.log("run...");

        this.network = this.getModel();

        UIServer uiServer = UIServer.getInstance();
        StatsStorage statsStorage = new InMemoryStatsStorage();
        this.network.setListeners(new StatsListener(statsStorage, listenerFreq));
        uiServer.attach(statsStorage);

        this.network.init();
        this.network.setListeners(new ScoreIterationListener(listenerFreq), new StatsListener(statsStorage, listenerFreq));

        this.trainModel();
        //TODO: Redirect default logging to application logger
        //TODO: split threads

        this.save();

        this.logger.log("Network weight params: " + network.params().toString());
        this.logger.log("Done");
    }

    protected void trainModel() throws Exception {
        this.logger.log("Load data....");
        /**
         * Data Setup -> organize and limit data file paths:
         *  - mainPath = path to image files
         *  - fileSplit = define basic dataset split with limits on format
         *  - pathFilter = define additional file load filter to limit size and balance batch content
         **/
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        File mainPath = new File(this.session.getPath());
        FileSplit fileSplit = new FileSplit(mainPath, NativeImageLoader.ALLOWED_FORMATS, rng);
        BalancedPathFilter pathFilter = new BalancedPathFilter(rng, labelMaker, this.session.getNumExamples(), this.session.getNumLabels(), this.session.getBatchSize());

        /**
         * Data Setup -> train test split
         *  - inputSplit = define train and test split
         **/
        InputSplit[] inputSplit = fileSplit.sample(pathFilter, this.session.getNumExamples() * (1 + splitTrainTest), this.session.getNumExamples() * (1 - splitTrainTest));
        InputSplit trainData = inputSplit[0];
        InputSplit testData = inputSplit[1];

        ArrayList<ImageTransform> transforms = this.session.getTransformations();

        /**
         * Data Setup -> normalization
         *  - how to normalize images and generate large dataset to train on
         **/
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        this.logger.log("Build model....");

        /**
         * Data Setup -> define how to load data into net:
         *  - recordReader = the reader that loads and converts image data pass in inputSplit to initialize
         *  - dataIter = a generator that only loads one batch at a time into memory to save memory
         *  - trainIter = uses MultipleEpochsIterator to ensure model runs through the data for all epochs
         **/
        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);
        DataSetIterator dataIter;
        MultipleEpochsIterator trainIter;

        this.logger.log("Train model without transformations....");
        // Train without transformations
        recordReader.initialize(trainData, null);
        dataIter = new RecordReaderDataSetIterator(recordReader, this.session.getBatchSize(), 1, this.session.getNumLabels());
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);
        trainIter = new MultipleEpochsIterator(this.session.getNumEpochs(), dataIter, this.session.getNCores());
        this.network.fit(trainIter);

        this.logger.log("Train model with transformations....");
        // Train with transformations
        for (ImageTransform transform : transforms) {
            System.out.print("\nTraining on transformation: " + transform.getClass().toString() + "\n\n");
            recordReader.initialize(trainData, transform);
            dataIter = new RecordReaderDataSetIterator(recordReader, this.session.getBatchSize(), 1, this.session.getNumLabels());
            scaler.fit(dataIter);
            dataIter.setPreProcessor(scaler);
            trainIter = new MultipleEpochsIterator(this.session.getNumEpochs(), dataIter, this.session.getNCores());
            network.fit(trainIter);
        }

        this.logger.log("Evaluate model....");
        recordReader.initialize(testData);
        dataIter = new RecordReaderDataSetIterator(recordReader, this.session.getBatchSize(), 1, this.session.getNumLabels());
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);
        Evaluation eval = network.evaluate(dataIter);
        this.logger.log(eval.stats(true));

        // Example on how to get predict results with trained model
        dataIter.reset();
        DataSet testDataSet = dataIter.next();
        String expectedResult = testDataSet.getLabelName(0);
        List<String> predict = network.predict(testDataSet);
        String modelResult = predict.get(0);
        this.logger.log("\nFor a single example that is labeled " + expectedResult + " the model predicted " + modelResult + "\n\n");
    }

    public void save() throws Exception {
        this.logger.log("Saving...");

//        NetSaverLoaderUtils.saveNetworkAndParameters(network, this.session.getModelsFolderPath());
//        NetSaverLoaderUtils.saveUpdators(network, this.session.getModelsFolderPath());
        Random r = new Random(seed);

        File locationToSave = new File(this.session.getModelsFolderPath() + r.toString() + "_MultiLayerNetwork.zip");
        ModelSerializer.writeModel(network, locationToSave, true);

        this.logger.log("Saved Successfully!");
    }

    public void setAlreadyTrained(boolean flag) {
        this.alreadyTrained = flag;
    }

    public boolean getAlreadyTrained() {
        return this.alreadyTrained;
    }

    public void setLogger(UILogger logger) {
        this.logger = logger;
    }
}
