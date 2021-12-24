package bgu.spl.mics.application.objects;


import bgu.spl.mics.application.services.GPUService;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {



	private class Statistics {
		private ConcurrentLinkedQueue<String> modelsTrained ;
		private AtomicInteger totalNumOfBatches;
		private AtomicInteger usedCPUTimeUnits;
		private AtomicInteger usedGPUTimeUnits;

		private Statistics() {
			this.modelsTrained = new ConcurrentLinkedQueue<String>();
			this.totalNumOfBatches = new AtomicInteger();
			this.usedCPUTimeUnits = new AtomicInteger();
			this.usedGPUTimeUnits = new AtomicInteger();
		}

	}

	public int getTotalNumOfBatches() {
		return stats.totalNumOfBatches.intValue();
	}

	public int getUsedCPUTimeUnits() {
		return stats.usedCPUTimeUnits.intValue();
	}

	public int getUsedGPUTimeUnits() {
		return stats.usedGPUTimeUnits.intValue();
	}


	/**
	 * Retrieves the single instance of this class.
	 */
	private Collection<GPU> GPU = new ArrayList<>();
	private Collection<CPU> CPUS = new ArrayList<>();
	private Statistics stats = new Statistics();


	private LinkedBlockingQueue<DataBatch> unprocessedBatches = new LinkedBlockingQueue<>();

	public static Cluster getInstance() {
		return Cluster.getCluster.instance ;
	}
	private static class getCluster{
		private static  Cluster instance = new Cluster() ;
	}

	public synchronized void GetFromGpu(DataBatch batch) {
		unprocessedBatches.add(batch);
		stats.usedGPUTimeUnits.addAndGet(batch.getTotalGpuProcessingTime());
	}
	public LinkedBlockingQueue<DataBatch> getUnprocessedBatches() {
		return unprocessedBatches;
	}

	public void CpuToGpu(DataBatch batch) { 	// Return processed batch from CPU back to the relevant GPU
		stats.totalNumOfBatches.addAndGet(1);
		batch.getGpu().receiveFromCluster(batch);
	}

	public void addModelToStats(Model model) {
		stats.modelsTrained.add(model.getName());

	}
	public void terminateCpus() { // Sending "poison" batches to indicate termination
		for (int i=0; i <CPUS.size(); i++)
			unprocessedBatches.add(new DataBatch(null));
	}

	public Collection<GPU> getGPUS() {
		return GPU;
	}

	public Collection<CPU> getCPUS() {
		return CPUS;
	}

	public void addCpuTimeUsed(int i) {
		stats.usedCPUTimeUnits.addAndGet(1);
	}

}
