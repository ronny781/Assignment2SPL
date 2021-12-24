package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;


import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {
    private final GPU gpu;
    private final PriorityQueue<TrainModelEvent> trainEvents;
    private final LinkedBlockingQueue<Message> otherMessages;
    private LinkedBlockingQueue<Message> mbQueue;
    private TrainModelEvent event;
    private Boolean isCounting = false;


    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
        this.trainEvents = new PriorityQueue<>(new Comparator<TrainModelEvent>() {
            @Override
            public int compare(TrainModelEvent t1, TrainModelEvent t2) {
                return Integer.compare(t1.calculateTime(), t2.calculateTime());
            }
        });
        this.otherMessages = new LinkedBlockingQueue<>();
    }

    @Override
    protected void initialize() {
        this.mbQueue = messageBus.getMicroMap().get(this);

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
            if (tick.isTerminate()) {
                terminate();
                gpu.getCluster().terminateCpus();
            } else {
                gpu.updateCurrentTime();
                gpu.sendBatchToCluster();
                if (isCounting) {
                    LinkedBlockingQueue<DataBatch> vRam = gpu.getvRamBatches();
                    if (!vRam.isEmpty()) {
                        vRam.peek().updateTimeUntilTrained();
                        if (vRam.peek().getTimeUntilTrained() == 0) {
                            Data data = vRam.peek().getData();
                            gpu.getTrainedBatches().add(gpu.getvRamBatches().remove());
                            gpu.updateVramSpace();
                            if (gpu.getTrainedBatches().size() * 1000 == data.getSize()) {
                                messageBus.complete(event, true);
                                gpu.getCluster().addModelToStats(event.getModel());
                                event.getModel().setStatus(Model.Status.Trained);
                                gpu.getTrainedBatches().clear();
                                isCounting = false;
                                System.out.println(event.getModel().getName() + " is Finished");
                            }
                        }
                    }
                } else {
                    while (!otherMessages.isEmpty()) {
                        Message e = otherMessages.remove();
                        this.CallBackMap.get(e.getClass()).call(e);
                    }
                    if (!trainEvents.isEmpty()) {
                        TrainModelEvent t = trainEvents.remove();
                        this.CallBackMap.get(t.getClass()).call(t);
                    }
                }
            }
        });

        subscribeEvent(TrainModelEvent.class, (TrainModelEvent trainEvent) -> {
            if (!isCounting) {
                System.out.println("Training Event " + trainEvent.getModel().getName() + "GPU " + gpu.getType() +" ");
                isCounting = true;
                event = trainEvent;
                Data data = trainEvent.getModel().getData();
                gpu.divideAndStore(data);
                gpu.sendBatchToCluster();
            } else
                trainEvents.add(trainEvent);


        });
        subscribeEvent(TestModelEvent.class, (TestModelEvent testEvent) -> {
            if (!isCounting) {
                System.out.println("Testing Event " + testEvent.getModel().getName());
                Model model = testEvent.getModel();
                double rand = Math.random();
                Student.Degree degreeType = model.getStudent().getDegree();
                if (degreeType == Student.Degree.MSc)
                    model.setResults(rand <= 0.6 ? Model.Results.Good : Model.Results.Bad);
                else
                    model.setResults(rand <= 0.8 ? Model.Results.Good : Model.Results.Bad);
                model.setStatus(Model.Status.Tested);
                messageBus.complete(testEvent, model.getResults());
            } else
                otherMessages.add(testEvent);
        });


    }
}
