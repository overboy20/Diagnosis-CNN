package diagnosis.Utilities;

import diagnosis.controller.VisualizationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WindowsManager {

    public static final int WINDOW_SELECT_MODEL = 1;
    public static final int WINDOW_MODEL = 2;
    public static final int WINDOW_VISUALIZE_MODEL = 3;
    public static final int WINDOW_LOAD_MODEL_WINDOW = 4;

    public void openWindow(int windowType) throws Exception {
        String resource;
        Pane root;
        String title;

        switch(windowType) {
            case WINDOW_SELECT_MODEL:
                resource = "/fxml/selectModelWindow.fxml";
                title = "Select Model";
                break;
            case WINDOW_MODEL:
                resource = "/fxml/modelWindow.fxml";
                title = "New Model";
                break;
            case WINDOW_VISUALIZE_MODEL:
                resource = "/fxml/VisualizeWindow.fxml";
                title = "Visualize Model";
                break;
            case WINDOW_LOAD_MODEL_WINDOW:
                resource = "/fxml/LoadModelWindow.fxml";
                title = "Load Model";
                break;
            default:
                resource = "";
                title = "";
        }

        Stage window = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        root = loader.load();

        Scene scene = new Scene(root);
        window.setScene(scene);
        window.setResizable(false);
        window.setTitle(title);
        window.show();

        VisualizationController controller = loader.getController(); //TODO
        controller.init();
    }
}
