package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {
    private final String name;
    private final Data data;
    private final Student student;
    private Status status;
    private Results results;


    public enum Status {PreTrained, Training, Trained, Tested}

    public enum Results {None, Good, Bad}

    public String getStatusAsString() {
        if (status == Status.Tested)
            return "Tested";
        else if (status == Status.Trained)
            return "Trained";
        else if (status == Status.Training)
            return "Training";
        else
            return "PreTrained";
    }

    public String getResultsAsString() {
        if (results == Results.Good)
            return "Good";
        else if (results == Results.Bad)
            return "Bad";
        else
            return "None";
    }

    public Student getStudent() {
        return student;
    }

    public String getName() {
        return name;
    }

    public void setResults(Results res) {
        results = res;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status s) {
        this.status = s;
    }

    public Results getResults() {
        return results;
    }

    public Model(String name, Data data, Student student, Status status, Results results) {
        this.name = name;
        this.data = data;
        this.student = student;
        this.status = status;
        this.results = results;
    }

    public Data getData() {
        return data;
    }

}


