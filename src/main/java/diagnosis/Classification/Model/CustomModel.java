package diagnosis.Classification.Model;

import diagnosis.Classification.Helpers.TrainingSession;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public class CustomModel extends Model {

    public CustomModel(TrainingSession session) {
        super(session);
    }

    @Override
    public MultiLayerNetwork getModel() {
        return null; //TODO
    }
}
