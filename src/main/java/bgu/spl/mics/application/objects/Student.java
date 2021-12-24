package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {

    /**
     * Enum representing the Degree the student is studying for.
     */
    public enum Degree {
        MSc, PhD
    }

    private final String name;
    private final String department;
    private final Degree degree;
    private int publications;
    private int papersRead;
    private final List<Model> listOfModels;
    private final List<Model> trainedModels;


    public Student(String name, String department, Degree degree, int publications, int papersRead) {
        this.name = name;
        this.department = department;
        this.degree = degree;
        this.publications = publications;
        this.papersRead = papersRead;
        this.listOfModels = new ArrayList<>();
        this.trainedModels = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public int getPublications() {
        return publications;
    }

    public int getPapersRead() {
        return papersRead;
    }

    public Degree getDegree() {
        return degree;
    }

    public String getDegreeAsString() {
        return degree == Degree.MSc ? "MSc" : "PhD";
    }

    public List<Model> getListOfModels() {
        return listOfModels;
    }

    public List<Model> getTrainedModels() {
        return trainedModels;
    }

    public void increasePublications(int num) {
        this.publications += num;
    }

    public void increasePapersRead(int num) {
        this.papersRead += num;
    }
}
