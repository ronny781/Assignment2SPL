package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateTimeService;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {

    private int currentTime = 1;
    private final Timer timer;
    private final long speed;
    private final long duration;

    public TimeService(long speed, long duration) {
        super("TimeService");
        this.timer = new Timer();
        this.speed = speed;
        this.duration = duration;

    }

    @Override
    protected void initialize() {

        subscribeBroadcast(TerminateTimeService.class, (TerminateTimeService terminateTimeService) -> {
            terminate();

        });
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (currentTime > duration) {
                    messageBus.sendBroadcast(new TickBroadcast(true));
                    sendBroadcast(new TerminateTimeService());
                    timer.cancel();
                    return;

                } else {
                    messageBus.sendBroadcast(new TickBroadcast(false));
                    currentTime += speed;
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, speed);


    }

}


