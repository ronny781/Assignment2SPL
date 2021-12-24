package bgu.spl.mics.application.objects;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int numOfCores;
    private Cluster cluster;
    private int currentTime = 0;
    private DataBatch currentBatch;
    private boolean isProcessing = false;
    private int timeUntilFinished;


    public CPU(int numOfCores, Cluster cluster) {
        this.numOfCores = numOfCores;
        this.cluster = cluster;
    }
    public int calculateProcessingTime(DataBatch batch){
        Data.Type type = batch.getData().getType();
        int processingTime = (32/numOfCores);
        if(type == Data.Type.Images){
            processingTime *= 4;
        }
        else if(type == Data.Type.Text){
            processingTime *= 2;
        }
        //For tabular return processingTime
        return processingTime;
    }
    public void startProcessing()  {
        isProcessing = true;
        int processingTime = calculateProcessingTime(currentBatch);
        timeUntilFinished = processingTime;
        currentBatch.setTotalCpuProcessingTime(processingTime);
    }

    public boolean takeBatch() throws InterruptedException {
        currentBatch = cluster.getUnprocessedBatches().take();
        if (currentBatch.getData() == null)
            return false;
        startProcessing();
        return true;
    }

    public void sendToCluster(){
        if (currentBatch != null)
            cluster.CpuToGpu(currentBatch);
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void updateCurrentTime() {
        currentTime++;
    }

    public void updateTimeUntilFinished() {
        timeUntilFinished --;
        if (timeUntilFinished == 0)
            isProcessing = false;
    }


    public Cluster getCluster() {
        return cluster;
    }

}
