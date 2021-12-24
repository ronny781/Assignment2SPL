package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    public boolean terminate;

    public TickBroadcast(boolean terminate) {
        this.terminate = terminate;
    }

    public boolean isTerminate() {
        return terminate;
    }
}
