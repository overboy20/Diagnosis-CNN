package diagnosis.Classification.Model;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public interface ModelInterface {
    public static final int MODEL_ALEXNET = 1;
    public static final int MODEL_LENET   = 2;
    public static final int MODEL_CUSTOM  = 3;

    public MultiLayerNetwork getModel();
}
