package diagnosis.controller;

import diagnosis.Classification.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {
    @FXML
    private TextArea log;

    public void init() {
        log.setText("");
        log.setText("good");
        log.setText(log.getText() + "\n" + "very good");
    }
}
