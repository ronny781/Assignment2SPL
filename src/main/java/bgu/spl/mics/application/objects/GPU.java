package bgu.spl.mics.application.objects;

import bgu.spl.mics.Event;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {


    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Model model;
    private final Cluster cluster;
    private final List<DataBatch> diskBatches;
    private final LinkedBlockingQueue<DataBatch> vRamBatches;
    private final LinkedBlockingQueue<DataBatch> trainedBatches;
    private int currentTime = 0;
    private final int timeToTrain;
    private int vRamSpace;


    public LinkedBlockingQueue<DataBatch> getTrainedBatches() {
        return trainedBatches;
    }

    public static Type getTypeFromString(String type) {
        if (type.equals("RTX3090"))
            return GPU.Type.RTX3090;
        else if (type.equals("RTX2080"))
            return GPU.Type.RTX2080;
        else
            return GPU.Type.GTX1080;
    }

    public GPU(Type type, Model model, Cluster cluster) {
        this.type = type;
        this.model = model;
        this.cluster = cluster;
        this.diskBatches = new LinkedList<>();
        this.trainedBatches = new LinkedBlockingQueue<>();
        int size;
        if (this.type == Type.RTX3090) {
            this.timeToTrain = 1;
            size = 32;
        } else if (this.type == Type.RTX2080) {
            size = 16;
            this.timeToTrain = 2;
        } else {
            size = 8;
            this.timeToTrain = 4;
        }
        this.vRamBatches = new LinkedBlockingQueue<>();
        this.vRamSpace = size;
    }

    public void divideAndStore(Data data) { // Divide the data to units of data batches
        for (int i = 0; i < data.getSize(); i += 1000) {
            diskBatches.add(new DataBatch(data, i, this));
        }
    }

    public void sendBatchToCluster() { // Sending batch to cluster for cpu process
        if (!diskBatches.isEmpty() && vRamSpace > 0) {
            DataBatch batch = diskBatches.remove(0);
            cluster.GetFromGpu(batch);
            vRamSpace--;
        }
    }

    public synchronized void receiveFromCluster(DataBatch batch) {
        vRamBatches.add(batch);
    }

    public void updateCurrentTime() {
        currentTime++;
    }

    public void updateVramSpace() {
        vRamSpace++;
    }

    public LinkedBlockingQueue<DataBatch> getvRamBatches() {
        return vRamBatches;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Cluster getCluster() {
        return cluster;
    }

}
