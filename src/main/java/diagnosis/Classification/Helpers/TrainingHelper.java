package diagnosis.Classification.Helpers;

import diagnosis.Classification.Model.Model;

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

    protected String path;
    protected int epochs;
    protected int iterations;

    // Image transformations
    protected boolean flipRandom;
    protected boolean warpTransformation;
    protected boolean colorTransformation;

    protected double learningRate;
    protected String activationFunction;

    public TrainingHelper(int epochs, int iterations) {
        this.epochs = epochs;
        this.iterations = iterations;
    }

    public void setModelType(int type) {
        this.modelType = type;
    }

    public void setTransformations(boolean flip, boolean warp, boolean color) {
        this.flipRandom = flip;
        this.warpTransformation = warp;
        this.colorTransformation = color;
    }

    public void setAdditionalParameters(double learningRate, String activation) {
        this.learningRate = learningRate;
        this.activationFunction = activation;
    }

    public void start() {
        this.model.run();
    }
}
