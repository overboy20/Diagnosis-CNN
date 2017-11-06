package diagnosis.Classification.Model;

import diagnosis.Classification.Helpers.TrainingSession;
import diagnosis.Utilities.UILogger;
import org.apache.commons.io.FilenameUtils;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.ColorConversionTransform;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.WarpImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.MultipleEpochsIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.*;
import org.deeplearning4j.nn.conf.distribution.GaussianDistribution;
import org.deeplearning4j.nn.conf.distribution.NormalDistribution;
import org.deeplearning4j.nn.conf.inputs.InvalidInputTypeException;
import org.deeplearning4j.nn.conf.layers.LocalResponseNormalization;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.NetSaverLoaderUtils;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

    protected ParentPathLabelGenerator labelMaker;
    protected InputSplit trainData;
    protected InputSplit testData;

    public Model(TrainingSession session) {
        this.session = session;
    }

    abstract public MultiLayerNetwork getModel();

    public void run() throws Exception {
        this.logger.insertLine("run...");
        this.network = this.getModel();

        this.dataSetup();

        this.network.init();
        this.network.setListeners(new ScoreIterationListener(listenerFreq));

        this.trainWithoutTransformations();
        this.trainWithTransformations();
        this.evaluateModel();
    }

    public void dataSetup() {
        this.logger.insertLine("Data Setup...");

        int numExamples = this.session.getNumExamples();

        this.labelMaker = new ParentPathLabelGenerator();
        File mainPath = new File(this.session.getPath());
        FileSplit fileSplit = new FileSplit(mainPath, NativeImageLoader.ALLOWED_FORMATS, rng);
        BalancedPathFilter pathFilter = new BalancedPathFilter(rng, this.labelMaker, numExamples, this.session.getNumLabels(), this.session.getBatchSize());

        InputSplit[] inputSplit = fileSplit.sample(pathFilter, numExamples * (1 + splitTrainTest), numExamples * (1 - splitTrainTest));
        this.trainData = inputSplit[0];
        this.testData = inputSplit[1];

        this.logger.insertLine("Data Setup Finished.");
    }

    protected void trainWithTransformations() throws Exception {
        this.logger.insertLine("Start training with transformations...");

        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, this.labelMaker);
        DataSetIterator dataIterator;
        MultipleEpochsIterator trainIter;
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);

        ImageTransform flipTransform1 = new FlipImageTransform(rng);
        ImageTransform flipTransform2 = new FlipImageTransform(new Random(123));
        ImageTransform warpTransform = new WarpImageTransform(rng, 42);
        //ImageTransform colorTransform = new ColorConversionTransform(new Random(seed), COLOR_BGR2YCrCb);
        List<ImageTransform> transforms = Arrays.asList(new ImageTransform[]{flipTransform1, warpTransform, flipTransform2});

        for (ImageTransform transform : transforms) {
            System.out.print("\nTraining on transformation: " + transform.getClass().toString() + "\n\n");
            recordReader.initialize(trainData, transform);
            dataIterator = new RecordReaderDataSetIterator(recordReader, this.session.getBatchSize(), 1, this.session.getNumLabels());
            scaler.fit(dataIterator);
            dataIterator.setPreProcessor(scaler);
            trainIter = new MultipleEpochsIterator(this.session.getNumEpochs(), dataIterator, this.session.getNCores());
            network.fit(trainIter);

            this.logger.insertLine("Training with transformations finished.");
        }
    }

    protected void trainWithoutTransformations() throws Exception {
        this.logger.insertLine("Start training without transformations...");

        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, this.labelMaker);
        DataSetIterator dataIterator;
        MultipleEpochsIterator trainIter;
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);

        recordReader.initialize(trainData, null);
        dataIterator = new RecordReaderDataSetIterator(recordReader, this.session.getBatchSize(), 1, this.session.getNumLabels());
        scaler.fit(dataIterator);
        dataIterator.setPreProcessor(scaler);
        trainIter = new MultipleEpochsIterator(this.session.getNumEpochs(), dataIterator, this.session.getNCores());
        network.fit(trainIter);

        this.logger.insertLine("Training without transformations finished.");
    }

    protected void evaluateModel() throws Exception {
        this.logger.insertLine("Start Evaluate model...");

        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, this.labelMaker);
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);

        recordReader.initialize(testData);
        DataSetIterator dataIterator;
        dataIterator = new RecordReaderDataSetIterator(recordReader, this.session.getBatchSize(), 1, this.session.getNumLabels());
        scaler.fit(dataIterator);
        dataIterator.setPreProcessor(scaler);
        Evaluation eval = network.evaluate(dataIterator);

        this.logger.insertLine("Model Evaluation finished.");
        this.logger.insertLine(eval.stats(true));

        this.getPrediction(dataIterator);
    }

    protected void getPrediction(DataSetIterator dataIter) {
        dataIter.reset();
        DataSet testDataSet = dataIter.next();
        String expectedResult = testDataSet.getLabelName(0);
        List<String> predict = network.predict(testDataSet);
        String modelResult = predict.get(0);

        this.logger.insertLine("\nFor a single example that is labeled " + expectedResult + " the model predicted " + modelResult + "\n\n");
    }

    public void save() {
        this.logger.insertLine("Saving...");

        String basePath = FilenameUtils.concat(System.getProperty("user.dir"), this.session.getPath());
        NetSaverLoaderUtils.saveNetworkAndParameters(network, basePath);
        NetSaverLoaderUtils.saveUpdators(network, basePath);

        this.logger.insertLine("Saved Successfully!");
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
