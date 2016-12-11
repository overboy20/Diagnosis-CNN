package diagnosis.Classification.Model;

import org.apache.commons.io.FilenameUtils;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.NetSaverLoaderUtils;

public class ModelBuilder {
    public void createModel() {

    }

    public void loadModel() {

    }

    public void saveModel(MultiLayerNetwork network) {
        String basePath = FilenameUtils.concat(System.getProperty("user.dir"), "dl4j-examples/src/main/resources/");
        NetSaverLoaderUtils.saveNetworkAndParameters(network, basePath);
        NetSaverLoaderUtils.saveUpdators(network, basePath);
    }
}
