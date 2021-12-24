package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.conferenceInformation;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConferenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    private int currentTime;
    conferenceInformation conference;

    public ConferenceService(String name, conferenceInformation conference) {
        super(name);
        this.conference = conference;
        this.currentTime = 1;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
            currentTime++;
            if (currentTime == conference.getDate()) {
                sendBroadcast(new PublishConferenceBroadcast(conference.getGoodResults()));
                terminate();
            }

        });

        subscribeEvent(PublishResultsEvent.class, (PublishResultsEvent publishResults) -> {
            System.out.println("Publishing Event " + publishResults.getModel().getName());
            conference.getGoodResults().add(publishResults.getModel());
        });
    }
}
