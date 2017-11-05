package diagnosis.controller;

import diagnosis.Classification.Model.ModelInterface;
import javafx.fxml.FXML;

public class ModelController {

    protected int MODEL_TYPE = ModelInterface.MODEL_ALEXNET;

    public void init() {
        System.out.println("init model controller");
    }

    public void setModelType(int type) {
        this.MODEL_TYPE = type;
    }
}
