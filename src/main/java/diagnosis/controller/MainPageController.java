package diagnosis.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {

    @FXML private VBox root;

    @FXML public void createNewModelClickHandler() throws IOException {
        Stage modelWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/selectModelWindow.fxml"));
        HBox root = loader.load();
        Scene scene = new Scene(root);
        modelWindow.setScene(scene);
        modelWindow.setResizable(false);
        modelWindow.show();

        Stage currentStage = (Stage) this.root.getScene().getWindow();
        currentStage.close();

        SelectModelController controller = loader.getController();
        controller.init();
    }

    @FXML public void loadModelClickHandler() throws Exception {
        Stage modelWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoadModelWindow.fxml"));
        VBox root = loader.load();
        Scene scene = new Scene(root);
        modelWindow.setScene(scene);
        modelWindow.setResizable(false);
        modelWindow.setTitle("Load Model");
        modelWindow.show();
    }

    public void init() { }
}
