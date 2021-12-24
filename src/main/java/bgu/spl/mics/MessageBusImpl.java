package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {


    private final ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microMap = new ConcurrentHashMap<>();


    private final ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> messageSubscribers = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Event, Future> toResolve = new ConcurrentHashMap<>();// Added " = new Conc...." myself

    public ConcurrentHashMap<Event, Future> getToResolve() {
        return toResolve;
    }

    public ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> getMicroMap() {
        return microMap;
    }


    private static class getMessageBus {
        private static final MessageBusImpl instance = new MessageBusImpl();
    }

    public static MessageBusImpl getInstance() {
        return getMessageBus.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        // TODO Auto-generated method stub
        subscribe(type, m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        // TODO Auto-generated method stub
        subscribe(type, m);
    }

    private void subscribe(Class<? extends Message> type, MicroService m) { // Creating new Queue for a specific type, need to be synchronized(I think the entire method should be)
        synchronized (type.getClass()) {
            if (messageSubscribers.containsKey(type) && !messageSubscribers.get(type).contains(m)) {
                messageSubscribers.get(type).add(m);
            } else if (!messageSubscribers.containsKey(type)) {
                messageSubscribers.put(type, new ConcurrentLinkedQueue<>());
                messageSubscribers.get(type).add(m);
            }
        }
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        // TODO Auto-generated method stub
        toResolve.get(e).resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        // TODO Auto-generated method stub

        if (messageSubscribers.get(b.getClass()) != null) // Added myself
            for (MicroService ms : messageSubscribers.get(b.getClass())) {
                if (microMap.get(ms) != null)
                    microMap.get(ms).add(b);
            }
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        // TODO Auto-generated method stub
        synchronized (e.getClass()) { //
            Future<T> future = new Future<>();
            toResolve.put(e, future);
            MicroService nextInLineMS = messageSubscribers.get(e.getClass()).poll();
            messageSubscribers.get(e.getClass()).add(nextInLineMS);
            microMap.get(nextInLineMS).add(e);
            return future;
        }
    }

    @Override
    public void register(MicroService m) {
        microMap.put(m, new LinkedBlockingQueue<Message>());

    }


    public void unregister(MicroService m) {

        for (Class<? extends Message> type : m.getListOfTypes()) {
            messageSubscribers.get(type).remove(m);
        }
        for (Message i : microMap.get(m)) {
            Future<Boolean> future = toResolve.get(i);
            if (future != null) { // if its broadcast it will not enter
                future.resolve(null);
            }
        }
        microMap.remove(m);

    }

    public ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> getMessageSubscribers() {
        return messageSubscribers;
    }


    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        // TODO Auto-generated method stub
        return microMap.get(m).take(); // take will wait until the element is available
    }

    @Override
    public boolean isMicroServiceRegistered(MicroService m) {
        return microMap.containsKey(m);
    }


}
