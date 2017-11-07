package diagnosis.controller;

import diagnosis.Classification.Helpers.TrainingHelper;
import diagnosis.Classification.Helpers.TrainingSession;
import diagnosis.Classification.Model.Model;
import diagnosis.Utilities.UILogger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class ModelController {

    @FXML protected TextField inputPath;
    @FXML protected Button    btnSelectPath;

    @FXML protected Slider sliderEpochs;
    @FXML protected Slider sliderIterations;

    @FXML protected CheckBox boxFlipRandom;
    @FXML protected CheckBox boxColor;
    @FXML protected CheckBox boxWrap;

    @FXML protected TextField   inputLearningRate;
    @FXML protected ComboBox    checkboxActivation;
    @FXML protected Button      btnStartTraining;
    @FXML protected TextArea    textareaLog;

    @FXML protected MenuItem menuitemSave;
    @FXML protected MenuItem menuitemClose;

    protected int MODEL_TYPE = Model.MODEL_ALEXNET;

    public void init() {
        this.inputPath.setText(System.getProperty("user.dir") + "/src/main/resources/images"); //Default path

        this.initSliders();
        this.initActivationsOptions();
        this.initCheckboxStates();

        this.bindEvents();
    }

    public void setModelType(int type) {
        this.MODEL_TYPE = type;
    }

    protected void initSliders() {
        this.sliderEpochs.setMin(0);
        this.sliderEpochs.setMax(100);
        this.sliderEpochs.setValue(20);
        this.sliderEpochs.setShowTickLabels(true);
        this.sliderEpochs.setShowTickMarks(true);
        this.sliderEpochs.setMajorTickUnit(10);
        this.sliderEpochs.setMinorTickCount(5);
        this.sliderEpochs.setBlockIncrement(10);

        this.sliderEpochs.valueProperty().addListener((obs, oldval, newVal) ->
                this.sliderEpochs.setValue(newVal.intValue()));

        this.sliderIterations.setMin(0);
        this.sliderIterations.setMax(5);
        this.sliderIterations.setValue(1);
        this.sliderIterations.setShowTickLabels(true);
        this.sliderIterations.setShowTickMarks(true);
        this.sliderIterations.setMajorTickUnit(1);
        this.sliderIterations.setMinorTickCount(1);
        this.sliderIterations.setBlockIncrement(1);

        this.sliderIterations.valueProperty().addListener((obs, oldval, newVal) ->
                this.sliderIterations.setValue(newVal.intValue()));
    }

    public void initCheckboxStates() {
        this.boxFlipRandom.setSelected(true);
        this.boxWrap.setSelected(true);
    }

    protected void initActivationsOptions() {
        this.checkboxActivation.getItems().addAll(
            TrainingHelper.ACTIVATION_FUNCTION_RELU,
            TrainingHelper.ACTIVATION_FUNCTION_SIGMOID,
            TrainingHelper.ACTIVATION_FUNCTION_ELU,
            TrainingHelper.ACTIVATION_FUNCTION_LEAKY_RELU,
            TrainingHelper.ACTIVATION_FUNCTION_SOFTMAX,
            TrainingHelper.ACTIVATION_FUNCTION_SOFTPLUS,
            TrainingHelper.ACTIVATION_FUNCTION_SOFTSIGN,
            TrainingHelper.ACTIVATION_FUNCTION_TANH
        );

        this.checkboxActivation.getSelectionModel().select(0);
    }

    protected void bindEvents() {
        this.inputLearningRate.textProperty().addListener(
            (ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
                if (!newValue.matches("\\d*")) {
                    inputLearningRate.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
    }

    @FXML public void startTrainingClick() {
        Double epochs = this.sliderEpochs.getValue();
        Double iterations = this.sliderIterations.getValue();
        String learningRate = this.inputLearningRate.getText();

        TrainingSession session = new TrainingSession();
        session.initTransformations(this.boxFlipRandom.isSelected(), this.boxWrap.isSelected(), this.boxColor.isSelected());
        session.initTrainingOptions(iterations.intValue(), epochs.intValue());
        session.setImagesPath(this.inputPath.getText());

        TrainingHelper helper = new TrainingHelper(session);
        helper.setModelType(this.MODEL_TYPE);

        if(this.checkboxActivation.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        helper.setAdditionalParameters(Double.parseDouble(learningRate), this.checkboxActivation.getSelectionModel().getSelectedItem().toString());
        helper.start(new UILogger(this.textareaLog));
    }

    @FXML public void choosePathHandler() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose directory");
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File dir = chooser.showDialog(new Stage());
        inputPath.setText(dir.getAbsolutePath());
    }

    @FXML public void onClose() {
        Platform.exit();
    }

    @FXML public void onSave() {
        //TODO - chose folder dialog
    }
}
