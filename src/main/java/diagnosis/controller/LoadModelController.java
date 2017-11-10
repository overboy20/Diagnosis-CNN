package diagnosis.controller;

import diagnosis.Utilities.UILogger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.File;

public class LoadModelController {

    @FXML TextField inputModelLocation;
    @FXML Button btnOpenModelLocation;
    @FXML TextField inputImageLocation;
    @FXML Button btnOpenImageLocation;
    @FXML Button btnPredict;
    @FXML TextArea textareaPrediction;

    protected File image = null;
    protected String modelLocation = null;

    @FXML public void openModelClick() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose model location");
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File dir = chooser.showDialog(new Stage());

        if(dir != null) {
            modelLocation = dir.getAbsolutePath();
            inputModelLocation.setText(dir.getAbsolutePath());
        }
    }

    @FXML public void openImageClick() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select test image");
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            image = file;
            inputImageLocation.setText(file.getAbsolutePath());
        }
    }

    @FXML public void btnPredictClick() throws Exception {
        if(modelLocation != null && image != null) {
            UILogger logger = new UILogger(textareaPrediction);
            MultiLayerNetwork network = ModelSerializer.restoreMultiLayerNetwork(modelLocation);

            NativeImageLoader loader = new NativeImageLoader(100, 100, 3);
            INDArray indImage = loader.asMatrix(image);
            DataNormalization scaler = new ImagePreProcessingScaler(0,1);
            scaler.transform(indImage);

            INDArray output = network.output(indImage);

            logger.log("## The FILE CHOSEN WAS " + image.getAbsolutePath());
            logger.log("## The Neural Nets Pediction ##");
            logger.log("## list of probabilities per label ##");
            logger.log("## List of Labels in Order## ");
            logger.log(output.toString());
        }
    }
}
