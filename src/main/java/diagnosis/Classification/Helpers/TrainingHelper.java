package diagnosis.Classification.Helpers;

import diagnosis.Classification.Model.CustomModel;
import diagnosis.Classification.Model.Model;
import diagnosis.Classification.Model.Predefined.AlexNetModel;
import diagnosis.Classification.Model.Predefined.LenetModel;
import diagnosis.Utilities.UILogger;

public class TrainingHelper {

    public static final String ACTIVATION_FUNCTION_RELU = "ReLU";
    public static final String ACTIVATION_FUNCTION_LEAKY_RELU = "Leaky ReLU";
    public static final String ACTIVATION_FUNCTION_TANH = "Tanh";
    public static final String ACTIVATION_FUNCTION_SIGMOID = "Sigmoid";
    public static final String ACTIVATION_FUNCTION_SOFTMAX = "Softmax";
    public static final String ACTIVATION_FUNCTION_ELU = "ELU";
    public static final String ACTIVATION_FUNCTION_SOFTSIGN = "Softsign";
    public static final String ACTIVATION_FUNCTION_SOFTPLUS = "Softplus";

    protected int modelType;
    protected Model model;

    protected double learningRate;
    protected String activationFunction;
    protected TrainingSession session;

    public TrainingHelper(TrainingSession session) {
        this.session = session;
    }

    public void setModelType(int type) {
        this.modelType = type;
    }

    public void setAdditionalParameters(double learningRate, String activation) {
        this.learningRate = learningRate;
        this.activationFunction = activation;
    }

    public void start(UILogger logger) {
        try {
            this.model = this.getModelByType(this.modelType);
            this.model.setLogger(logger);

            this.model.run();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected Model getModelByType(int type) throws Exception {
        switch(type) {
            case Model.MODEL_ALEXNET:
                return new AlexNetModel(this.session);
            case Model.MODEL_LENET:
                return new LenetModel(this.session);
            case Model.MODEL_CUSTOM:
                return new CustomModel(this.session);
            default:
                throw new Exception("Invalid Training Model type");
        }
    }
}
