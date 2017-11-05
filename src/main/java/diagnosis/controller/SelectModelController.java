package diagnosis.controller;

import diagnosis.Classification.Model.ModelInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectModelController {

    @FXML private HBox root;

    public void init() {
        System.out.println("init select model controller");
    }

    @FXML public void lenetModel() throws IOException {
        this.openModelWindow(ModelInterface.MODEL_LENET);
    }

    @FXML public void alexnetModel() throws IOException {
        this.openModelWindow(ModelInterface.MODEL_ALEXNET);
    }

    @FXML public void customModel() throws IOException {
        this.openModelWindow(ModelInterface.MODEL_CUSTOM);
    }

    protected void openModelWindow(int modelType) throws IOException {
        Stage modelWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modelWindow.fxml"));
        VBox root = loader.load();
        Scene scene = new Scene(root);
        modelWindow.setScene(scene);
        modelWindow.setResizable(false);
        modelWindow.show();

        Stage currentStage = (Stage) this.root.getScene().getWindow();
        currentStage.close();

        ModelController controller = loader.getController();

        switch(modelType) {
            case ModelInterface.MODEL_ALEXNET:
                controller.setModelType(ModelInterface.MODEL_ALEXNET);
                break;
            case ModelInterface.MODEL_LENET:
                controller.setModelType(ModelInterface.MODEL_LENET);
                break;
            case ModelInterface.MODEL_CUSTOM:
                controller.setModelType(ModelInterface.MODEL_CUSTOM);
                break;
        }

        controller.init();
    }
}
