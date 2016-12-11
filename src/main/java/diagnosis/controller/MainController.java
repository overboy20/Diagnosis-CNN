package diagnosis.controller;

import diagnosis.Classification.*;
import diagnosis.Classification.Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import diagnosis.Utilities.UILogger;

public class MainController {
    @FXML private TextArea log;
    @FXML private CheckBox cbFlip;
    @FXML private CheckBox cbWarp;
    @FXML private CheckBox cbColor;

    UILogger logger;
    String modelType;

    @FXML public void createNewModelHandler() {
        logger.insertLine("new model");
        this.modelType = "new";
    }

    @FXML public void saveModelHandler() {
        logger.insertLine("save model");
    }

    @FXML public void loadModelHandler() {
        logger.insertLine("load model");
        this.modelType = "Loaded";
    }

    @FXML public void startTrainingHandler() throws Exception {
        logger.insertLine("Training started");

        boolean transformations[] = {
            cbFlip.isSelected(),
            cbWarp.isSelected(),
            cbColor.isSelected()
        };

        Model model = new Model(transformations);
        model.run();
    }

    public void init() {
        logger = new UILogger(this.log);

        //logger.insertLine("good");
        //logger.insertLine("very good");

        this.modelType = "";
    }
}
