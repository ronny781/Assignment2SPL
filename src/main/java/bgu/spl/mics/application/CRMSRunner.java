package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    private static final Cluster cluster = Cluster.getInstance();
    public static int gpuSize;

    public static void main(String[] args) throws IOException, InterruptedException {
        List<MicroService> microServicesList = new ArrayList<>();
        List<Student> studentsList = new ArrayList<>();
        List<conferenceInformation> ConferencesList = new ArrayList<>();
        List<Thread> threadList = new ArrayList<>();
        JsonElement fileElement = JsonParser.parseReader(new FileReader(args[0]));
        JsonObject jo = fileElement.getAsJsonObject();
        JsonArray arrayOfStudents = jo.getAsJsonArray("Students");
        JsonArray arrayOfGpus = jo.getAsJsonArray("GPUS");
        JsonArray arrayOfCpus = jo.getAsJsonArray("CPUS");
        JsonArray arrayOfConferences = jo.getAsJsonArray("Conferences");
        int tickTime = jo.get("TickTime").getAsInt();
        int duration = jo.get("Duration").getAsInt();
        for (int i = 0; i < arrayOfStudents.size(); i++) {
            JsonObject student = arrayOfStudents.get(i).getAsJsonObject();
            String studentName = student.get("name").getAsString();
            String studentDepartment = student.get("department").getAsString();
            String studentStatus = student.get("status").getAsString();
            JsonArray arrayOfModels = student.getAsJsonArray("models");
            Student s;
            if (studentStatus == "MSc")
                s = new Student(studentName, studentDepartment, Student.Degree.MSc, 0, 0);
            else
                s = new Student(studentName, studentDepartment, Student.Degree.PhD, 0, 0);
            //Student s = new Student(studentName, studentDepartment, studentStatus == "MSc" ? Student.Degree.MSc : Student.Degree.PhD, 0,0);// fix status enum
            for (int j = 0; j < arrayOfModels.size(); j++) {
                JsonObject model = arrayOfModels.get(j).getAsJsonObject();
                String modelName = model.get("name").getAsString();
                String dataType = model.get("type").getAsString();
                int modelSize = model.get("size").getAsInt();
                Data.Type daType;
                if (dataType.equals("images"))
                    daType = Data.Type.Images;
                else if (dataType.equals("Text"))
                    daType = Data.Type.Text;
                else
                    daType = Data.Type.Tabular;
                Data modelData = new Data(daType, 0, modelSize);
                Model m = new Model(modelName, modelData, s, Model.Status.PreTrained, Model.Results.None);
                s.getListOfModels().add(m);
            }
            studentsList.add(s);
            microServicesList.add(new StudentService(studentName, s));
        }

        gpuSize = arrayOfGpus.size();
        for (int i = 0; i < arrayOfGpus.size(); i++) { //create gpus
            String gpuType = arrayOfGpus.get(i).getAsString();
            GPU.Type type = GPU.getTypeFromString(gpuType);
            GPU gpu = new GPU(type, null, cluster);
            cluster.getGPUS().add(gpu);
            microServicesList.add(new GPUService("GPU " + (i + 1), gpu));
        }
        for (int i = 0; i < arrayOfCpus.size(); i++) {  //create cpus

            int numOfCores = arrayOfCpus.get(i).getAsInt();
            CPU cpu = new CPU(numOfCores, cluster);
            cluster.getCPUS().add(cpu);
            microServicesList.add(new CPUService("CPU " + (i + 1), cpu));
        }
        for (int i = 0; i < arrayOfConferences.size(); i++) { // create conferences
            JsonObject conference = arrayOfConferences.get(i).getAsJsonObject();
            String conferenceName = conference.get("name").getAsString();
            int conferenceDate = conference.get("date").getAsInt();
            conferenceInformation conferenceInformation = new conferenceInformation(conferenceName, conferenceDate);
            ConferencesList.add(conferenceInformation);
            microServicesList.add(new ConferenceService(conferenceName, conferenceInformation));
        }

        microServicesList.add(new TimeService(tickTime, duration));

        for (int i = 0; i < microServicesList.size(); i++) {
            Thread thread = new Thread(microServicesList.get(i));
            threadList.add(thread);
            thread.start();
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        // After all thread finished we will construct our output file.
        JsonObject output = new JsonObject();
        JsonArray studentsJsonArray = new JsonArray();

        for (Student student : studentsList) {
            JsonObject studentObject = new JsonObject();
            studentObject.addProperty("name", student.getName());
            studentObject.addProperty("department", student.getDepartment());
            studentObject.addProperty("status", student.getDegreeAsString());
            studentObject.addProperty("publications", student.getPublications());
            studentObject.addProperty("papersRead", student.getPapersRead());


            List<Model> trainedList = student.getTrainedModels();
            JsonArray trainedModels = new JsonArray();
            for (Model model : trainedList) {
                JsonObject modelObject = new JsonObject();
                modelObject.addProperty("name", model.getName());
                Data data = model.getData();
                JsonObject dataObject = new JsonObject();
                dataObject.addProperty("type", data.getTypeAsString());
                dataObject.addProperty("size", data.getSize());
                modelObject.add("data", dataObject);
                modelObject.addProperty("status", model.getStatusAsString());
                modelObject.addProperty("results", model.getResultsAsString());
                trainedModels.add(modelObject);
            }


            studentObject.add("trainedModels", trainedModels);
            studentsJsonArray.add(studentObject);
        }
        JsonArray conferencesJsonArray = new JsonArray();
        for (conferenceInformation conferenceInformation : ConferencesList) {
            JsonObject conferenceObject = new JsonObject();
            conferenceObject.addProperty("name", conferenceInformation.getName());
            conferenceObject.addProperty("date", conferenceInformation.getDate());
            JsonArray publicationsObject = new JsonArray();
            for (Model model : conferenceInformation.getGoodResults()) {
                JsonObject publicationObject = new JsonObject();
                publicationObject.addProperty("name", model.getName());
                Data data = model.getData();
                JsonObject dataObject = new JsonObject();
                dataObject.addProperty("type", data.getTypeAsString());
                dataObject.addProperty("size", data.getSize());
                publicationObject.add("data", dataObject);
                publicationObject.addProperty("status", model.getStatusAsString());
                publicationObject.addProperty("results", model.getResultsAsString());
                publicationsObject.add(publicationObject);
            }
            conferenceObject.add("publications", publicationsObject);
            conferencesJsonArray.add(conferenceObject);

        }
        output.add("students", studentsJsonArray);
        output.add("conferences", conferencesJsonArray);
        output.addProperty("cpuTimeUsed", cluster.getUsedCPUTimeUnits());
        output.addProperty("gpuTimeUsed", cluster.getUsedGPUTimeUnits());
        output.addProperty("batchesProcessed", cluster.getTotalNumOfBatches());
        System.out.println(output);
        try {
            FileWriter file = new FileWriter("output.json");
            file.write(output.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}