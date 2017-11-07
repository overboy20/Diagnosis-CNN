package diagnosis.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import diagnosis.Utilities.UILogger;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController {
    @FXML private TextArea log;
    @FXML private CheckBox cbFlip;
    @FXML private CheckBox cbWarp;
    @FXML private CheckBox cbColor;
    @FXML private TextField tfPathImages;
    @FXML protected ListView<String> listLayers;
    @FXML protected ListView<String> listClasses;

    private String folderPath;
    private String testImagePath;
    public final ObservableList<String> obsListClasses = FXCollections.observableArrayList();
    public final ObservableList<String> obsListLayers = FXCollections.observableArrayList();

    UILogger logger;
    String modelType;

    @FXML public void createNewModelHandler() {
        logger.log("new model");
        this.modelType = "new";
    }

    @FXML public void saveModelHandler() {
        logger.log("save model");
    }

    @FXML public void loadModelHandler() {
        logger.log("load model");
        this.modelType = "Loaded";
    }

    @FXML public void choosePathHandler() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose directory");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File dir = chooser.showDialog(new Stage());
        tfPathImages.setText(dir.getAbsolutePath());
    }

    @FXML public void startTrainingHandler() throws Exception {
        logger.log("Training started");

        boolean transformations[] = {
            cbFlip.isSelected(),
            cbWarp.isSelected(),
            cbColor.isSelected()
        };
    }

    public void init() {
        logger = new UILogger(this.log);

        listLayers.setItems(obsListLayers);
        listClasses.setItems(obsListClasses);

        obsListClasses.add("class 1");
        obsListClasses.add("class 2");
        obsListClasses.add("class 3");
        obsListClasses.add("class 4");
        obsListClasses.add("class 5");

        obsListLayers.add("Conv 3x3");
        obsListLayers.add("Pool 3x3");
        obsListLayers.add("Conv 5x5");
        obsListLayers.add("Pool 3x3");
        obsListLayers.add("Conv 3x3");
        obsListLayers.add("Conv 4x4");
        obsListLayers.add("Conv 5x5");
        obsListLayers.add("Pool 3x3");
        obsListLayers.add("MPL");

        this.modelType = "";
    }
}
