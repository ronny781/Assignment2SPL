package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.List;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */


public class StudentService extends MicroService {
    private final Student student;
    List<Model> listOfModels;
    Boolean testing = false;
    Boolean notNull = false;


    public StudentService(String name, Student student) {
        super(name);
        this.student = student;
        this.listOfModels = student.getListOfModels();

    }

    @Override
    protected void initialize() {
        int size = 0;
        while (!notNull)
            try {
                size = messageBus.getMessageSubscribers().get(TrainModelEvent.class).size();
                notNull = true;
            } catch (NullPointerException e) {
            }
        while (size < CRMSRunner.gpuSize) {
            size = messageBus.getMessageSubscribers().get(TrainModelEvent.class).size();
        }

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
            if (tick.isTerminate())
                terminate();
            else if (!listOfModels.isEmpty()) {
                Model.Status status = listOfModels.get(0).getStatus();
                if (status == Model.Status.PreTrained) {
                    sendEvent(new TrainModelEvent(listOfModels.get(0)));
                    listOfModels.get(0).setStatus(Model.Status.Training);

                } else if (status == Model.Status.Trained) {
                    if (!testing) {
                        sendEvent(new TestModelEvent(listOfModels.get(0)));
                        student.getTrainedModels().add(listOfModels.get(0));
                        testing = true;
                    }

                } else if (status == Model.Status.Tested) {
                    testing = false;
                    if (listOfModels.get(0).getResults() == Model.Results.Good)
                        sendEvent(new PublishResultsEvent(listOfModels.remove(0)));
                    else
                        listOfModels.remove(0);
                }
            }
        });
        subscribeBroadcast(PublishConferenceBroadcast.class, (PublishConferenceBroadcast publish) -> {
            List<Model> goodResults = publish.getGoodResults();
            int myPublishes = 0;
            int otherPublishes = 0;
            for (Model model : goodResults)
                if (model.getStudent() == student)
                    myPublishes++;
                else
                    otherPublishes++;
            this.student.increasePublications(myPublishes);
            this.student.increasePapersRead(otherPublishes);
        });

    }


}
