package diagnosis.Classification.Model;

import diagnosis.Classification.Helpers.TrainingSession;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public class CustomModel extends Model implements ModelInterface {

    public CustomModel(TrainingSession session) {
        super(session);
    }

    public MultiLayerNetwork getModel() {
        return null; //TODO
    }
}
