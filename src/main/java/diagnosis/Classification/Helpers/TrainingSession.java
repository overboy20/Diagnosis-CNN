package diagnosis.Classification.Helpers;

public class TrainingSession {
    boolean flipTransform;
    boolean warpTransform;
    boolean colorTransform;

    protected int batchSize = 20;
    protected int nCores = 2;

    protected int numExamples;
    protected int numLabels;

    protected int iterations;
    protected int epochs;

    public TrainingSession() {

    }

    public void initTransformations(boolean flip, boolean warp, boolean color) {
        this.flipTransform = flip;
        this.warpTransform = warp;
        this.colorTransform = color;
    }

    public void setBatchSize(int size) {
        this.batchSize = size;
    }

    public void setnCores(int num) {
        this.nCores = num;
    }


}
