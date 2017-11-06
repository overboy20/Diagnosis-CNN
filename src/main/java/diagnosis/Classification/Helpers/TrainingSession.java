package diagnosis.Classification.Helpers;

import java.util.ArrayList;

public class TrainingSession {
    boolean flipTransform;
    boolean warpTransform;
    boolean colorTransform;

    protected int batchSize = 20;
    protected int nCores = 2;

    protected int numExamples;
    protected ArrayList<String> classes;

    protected int iterations;
    protected int epochs;

    protected String path;

    public TrainingSession() { }

    public void initTransformations(boolean flip, boolean warp, boolean color) {
        this.flipTransform = flip;
        this.warpTransform = warp;
        this.colorTransform = color;
    }

    public void initTrainingOptions(int iterations, int epochs) {
        this.iterations = iterations;
        this.epochs = epochs;
    }

    public void setImagesPath(String path) {
        this.path = path;

        this.classes = this.parseClassesFromPath(path);
        this.numExamples = this.getExamplesNumber(path);
    }

    public void setBatchSize(int size) {
        this.batchSize = size;
    }

    public void setnCores(int num) {
        this.nCores = num;
    }

    protected ArrayList<String> parseClassesFromPath(String path) {
        ArrayList<String> classesList = new ArrayList<>();

        //TODO: parse classes

        return classesList;
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

    public int getNCores() {
        return this.nCores;
    }

    public String getPath() {
        return "src/main/resources/"; //TODO
//        return this.path;
    }

    public int getExamplesNumber(String path) {
        return 0;
    }
}
