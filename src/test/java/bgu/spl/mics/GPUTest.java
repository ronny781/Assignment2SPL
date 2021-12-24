//package bgu.spl.mics;
//
//import bgu.spl.mics.application.objects.*;
//import bgu.spl.mics.example.messages.ExampleEvent;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GPUTest {
//    private static GPU gpu;
//
//    @BeforeEach
//    void setUp() {
//        gpu = new GPU(null, null, null  );
//    }
//
//    @AfterEach
//    void tearDown() {
//        gpu = null;
//    }
//
//    @Test
//    void receiveEvent() {
//        Event<String> e = new ExampleEvent("Lior");
//        gpu.receiveEvent(e);
//        assertTrue(gpu.isEventReceived());
//    }
//
//    @Test
//    void divideAndStore() {
//        DataBatch batch = new DataBatch();
//        Data data = new Data(Data.Type.Tabular, 8, 5);
//        gpu.divideAndStore(data);
//        assertTrue(gpu.isDividedAndStored());
//    }
//
//    @Test
//    void sendBatch() {
//        DataBatch batch = new DataBatch();
//        gpu.sendBatch(batch);
//        assertTrue(gpu.isBatchSent());
//    }
//
//    @Test
//    void getType() {
//        assertEquals(gpu.getType(), null);
//    }
//
//    @Test
//    void setType() {
//        gpu.setType(GPU.Type.RTX2080);
//        assertEquals(GPU.Type.RTX2080, gpu.getType());
//    }
//
//    @Test
//    void getModel() {
//        assertEquals(gpu.getModel(), null);
//    }
//
//    @Test
//    void setModel() {
//        Model model = new Model("Lior",new Data(Data.Type.Tabular, 5, 7), new Student("Ronny", "CS", Student.Degree.MSc, 5, 5));
//        gpu.setModel(model);
//        assertEquals(model, gpu.getModel());
//    }
//
//    @Test
//    void getCluster() {
//        assertEquals(null, gpu.getCluster());
//    }
//
//    @Test
//    void setCluster() {
//        Cluster cluster = Cluster.getInstance();
//        gpu.setCluster(cluster);
//        assertEquals(cluster, gpu.getCluster());
//    }
//}