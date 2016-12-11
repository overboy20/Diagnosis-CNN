package diagnosis.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class AddLayerController {
    @FXML private ComboBox layerType;

    public MainController mainController;

    public void init(MainController c) {
        this.mainController = c;

        ObservableList<String> types =
                FXCollections.observableArrayList(
                        "Convolution",
                        "Pool",
                        "Fully connected"
                );

        layerType.setItems(types);
    }

    public void edit() {

    }

    public void addBefore() {

    }
}
