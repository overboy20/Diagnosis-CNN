package diagnosis.controller;

import diagnosis.Classification.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import diagnosis.Utilities.UILogger;

public class MainController {
    @FXML
    private TextArea log;
    UILogger logger;

    public void init() {
        logger = new UILogger(this.log);

        logger.insertLine("good");
        logger.insertLine("very good");
    }
}
