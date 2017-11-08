package diagnosis.controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class VisualizationController {
    @FXML WebView webViewComponent;

    public void init() {
        WebEngine webEngine = webViewComponent.getEngine();
        webEngine.load("http://localhost:9000");
    }
}
