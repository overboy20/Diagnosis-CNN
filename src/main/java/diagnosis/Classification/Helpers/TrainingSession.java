package diagnosis.Classification.Helpers;

import org.datavec.image.transform.ColorConversionTransform;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.WarpImageTransform;

import java.util.ArrayList;
import java.util.Random;

public class TrainingSession {
    boolean flipTransform;
    boolean warpTransform;
    boolean colorTransform;

    protected long seed = 42;
    protected Random rng = new Random(seed);

    protected int batchSize = 20;
    protected int nCores = 2;

    protected int numExamples;

    protected int iterations;
    protected int epochs;

    protected String path;

    public TrainingSession() { }

    public void initTransformations(boolean flip, boolean warp, boolean color) {
        this.flipTransform = flip;
        this.warpTransform = warp;
        this.colorTransform = color;
    }

    public ArrayList<ImageTransform> getTransformations() {
        ArrayList<ImageTransform> array = new ArrayList<>();

        if(this.flipTransform) {
            array.add(new FlipImageTransform(rng));
            array.add(new FlipImageTransform(new Random(123)));
        }

        if(this.warpTransform) {
            array.add(new WarpImageTransform(rng, 42));
        }

        if(this.colorTransform) {
            array.add(new ColorConversionTransform(new Random(seed), 36));
        }

        return array;
    }

    public void initTrainingOptions(int iterations, int epochs) {
        this.iterations = iterations;
        this.epochs = epochs;
    }

    public void setImagesPath(String path) {
        this.path = path;

        this.numExamples = this.getExamplesNumber(path);
    }

    public void setBatchSize(int size) {
        this.batchSize = size;
    }

    public void setnCores(int num) {
        this.nCores = num;
    }

    public int getNumExamples() {
        return 80; //TODO
//        return this.numExamples;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    public int getNumLabels() {
        return 4; //TODO
//        return this.classes.size();
    }

    public int getNumEpochs() {
        return this.epochs;
    }

    public int getNumIterations() {
        return this.iterations;
    }

    public int getNCores() {
        return this.nCores;
    }

    public String getPath() {
        return this.path;
    }

    public String getModelsFolderPath() {
        return System.getProperty("user.dir") + "/src/main/resources/Models";
    }

    public int getExamplesNumber(String path) {
        return 0;
    }
}
