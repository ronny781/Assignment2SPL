//package bgu.spl.mics;
//
//import bgu.spl.mics.application.objects.CPU;
//import bgu.spl.mics.application.objects.Cluster;
//import bgu.spl.mics.application.objects.DataBatch;
//import bgu.spl.mics.example.messages.ExampleEvent;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CPUTest {
//    private static CPU cpu;
//
//    @BeforeEach
//    void setUp() {
//        cpu = new CPU(0, null, null);
//    }
//
//    @AfterEach
//    void tearDown() {
//        cpu = null;
//    }
//
//    @Test
//    void receiveAndProcessBatch() {
//        DataBatch batch = new DataBatch();
//        cpu.receiveAndProcessBatch(batch);
//        assertTrue(cpu.isBatchReceivedAndProcessed());
//
//    }
//
//    @Test
//    void returnResults() {
//        DataBatch batch = new DataBatch();
//        cpu.returnResults(batch);
//        assertTrue(cpu.isResultsReturned());
//    }
//
//    @Test
//    void getNumOfCores() {
//        assertEquals(0, cpu.getNumOfCores());
//    }
//
//    @Test
//    void setNumOfCores() {
//        int num = 5;
//        cpu.setNumOfCores(num);
//        assertEquals(num, cpu.getNumOfCores());
//    }
//
//    @Test
//    void getDb() {
//        assertEquals(null, cpu.getDb());
//    }
//
//    @Test
//    void setDb() {
//        Collection<DataBatch> db = new ArrayList<>();
//        cpu.setDb(db);
//        assertEquals(db, cpu.getDb());
//    }
//
//    @Test
//    void getCluster() {
//        assertEquals(null, cpu.getCluster());
//    }
//
//    @Test
//    void setCluster() {
//        Cluster cluster = Cluster.getInstance();
//        cpu.setCluster(cluster);
//        assertEquals(cluster, cpu.getCluster());
//    }
//}