package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class conferenceInformation {

    private final String name;
    private final int date;
    private final List<Model> goodResults;

    public String getName() {
        return name;
    }

    public int getDate() {
        return date;
    }

    public List<Model> getGoodResults() {
        return goodResults;
    }

    public conferenceInformation(String name, int date) {
        this.name = name;
        this.date = date;
        goodResults = new ArrayList<Model>();
    }
}
