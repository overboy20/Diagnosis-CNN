package diagnosis.controller;

import diagnosis.Classification.Helpers.TrainingHelper;
import diagnosis.Classification.Model.CustomModel;
import diagnosis.Classification.Model.Model;
import diagnosis.Classification.Model.ModelInterface;
import diagnosis.Classification.Model.Predefined.AlexNetModel;
import diagnosis.Classification.Model.Predefined.LenetModel;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    @FXML protected ProgressBar progress;
    @FXML protected TextArea    textareaLog;

    @FXML protected MenuItem menuitemSave;
    @FXML protected MenuItem menuitemClose;

    protected int MODEL_TYPE = ModelInterface.MODEL_ALEXNET;

    public void init() {
        this.initSliders();
        this.initActivationsOptions();
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

        TrainingHelper helper = new TrainingHelper(epochs.intValue(), iterations.intValue());
        helper.setTransformations(this.boxFlipRandom.isSelected(), this.boxWrap.isSelected(), this.boxColor.isSelected());
        helper.setModelType(this.MODEL_TYPE);
        helper.setAdditionalParameters(Integer.parseInt(learningRate), this.checkboxActivation.getSelectionModel().getSelectedItem().toString());
        helper.start();
    }

    @FXML public void onClose() {
        Platform.exit();
    }

    @FXML public void onSave() {
        //TODO - chose folder dialog
    }
}
