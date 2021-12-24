package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PublishConferenceBroadcast implements Broadcast {

    private List<Model> goodResults;

    public PublishConferenceBroadcast(List goodResults) {
        this.goodResults = goodResults;
    }

    public List getGoodResults() {
        return goodResults;
    }
}
