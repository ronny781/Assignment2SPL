package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {
    private Data data;
    private int start_index;
    private GPU gpu;
    private int timeUntilTrained;
    private int totalGpuProcessingTime;
    private int totalCpuProcessingTime = 0;

    public void setTotalCpuProcessingTime(int totalCpuProcessingTime) {
        this.totalCpuProcessingTime = totalCpuProcessingTime;
    }

    public int getTotalGpuProcessingTime() {
        return totalGpuProcessingTime;
    }

    //Create "poison" batch
    public DataBatch(Data data) {
        this.data = null;
    }

    public DataBatch(Data data, int start_index, GPU gpu) {
        this.data = data;
        this.start_index = start_index;
        this.gpu = gpu;
        if(gpu.getType() == GPU.Type.RTX3090)
            this.timeUntilTrained = 1;
        else if(gpu.getType() == GPU.Type.RTX2080)
            this.timeUntilTrained = 2;
        else
            this.timeUntilTrained = 4;
        totalGpuProcessingTime = timeUntilTrained;
    }
    public GPU getGpu() {
        return gpu;
    }

    public Data getData() {
        return data;
    }

    public void updateTimeUntilTrained() {
        timeUntilTrained--;
    }

    public int getTimeUntilTrained() {
        return timeUntilTrained;
    }
}
