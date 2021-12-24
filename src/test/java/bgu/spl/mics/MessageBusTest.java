//package bgu.spl.mics;
//
//import bgu.spl.mics.example.messages.ExampleBroadcast;
//import bgu.spl.mics.example.messages.ExampleEvent;
//import bgu.spl.mics.example.services.ExampleMessageSenderService;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class MessageBusTest {
//    private static MessageBusImpl messageBus;
//    private static MicroService ms;
//    private static Future<?> f;
//
//    @Before
//    public void setUp() throws Exception{
//       messageBus = new MessageBusImpl();
//
//    }
//
//    @Test
//    void testSubscribeEvent(){
//        Event<String> e = new ExampleEvent("Lior");
//        String[] args = {"event"};
//        ms = new ExampleMessageSenderService("ronny",args);
////        messageBus.subscribeEvent(e.getClass(),ms);
//    }
//
//    @Test
//    void testSubscribeBroadcast(){
//        Broadcast b = new ExampleBroadcast("Hell");
//        String[] args = {"broadcast"};
//        ms = new ExampleMessageSenderService("ronny",args);
//        messageBus.subscribeBroadcast(b.getClass(),ms);
//        assertTrue(messageBus.isBroadcastSubscribed(b));
//    }
//
//    @Test
//    void testComplete(){
//       Event<String> e = new ExampleEvent("Lior");
//       messageBus.complete(e, "Ronny");
//       assertTrue(messageBus.isComplete(e,"Ronny"));
//    }
//
//    @Test
//    void testSendBroadcast(){
//        Broadcast b = new ExampleBroadcast("Hell");
//        messageBus.sendBroadcast(b);
//        assertTrue(messageBus.isBroadcastSent(b));
//
//    }
//
//    @Test
//    void testSendEvent() {
//        Event<String> e = new ExampleEvent("Lior");
//        messageBus.sendEvent(e);
//        assertTrue(messageBus.isEventSent(e));
//    }
//
//    @Test
//    void testRegister() {
//        String[] args = {"event"};
//        ms = new ExampleMessageSenderService("ronny", args);
//        messageBus.register(ms);
//        assertTrue(messageBus.isMicroServiceRegistered(ms));
//    }
//
//    @Test
//    void testUnregister() {
//        String[] args = {"event"};
//        ms = new ExampleMessageSenderService("ronny", args);
//        messageBus.unregister(ms);
//        assertFalse(messageBus.isMicroServiceRegistered(ms));
//    }
//
//    @Test
//    void testAwaitMessage() throws InterruptedException {
//        String[] args = {"broadcast"};
//        ms = new ExampleMessageSenderService("ronny", args);
//        messageBus.awaitMessage(ms);
//        assertTrue(messageBus.isMessageAwait(ms));
//    }
//
//
//}