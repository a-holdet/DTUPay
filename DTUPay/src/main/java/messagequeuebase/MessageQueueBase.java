package messagequeuebase;

import merchantservice.EventType;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MessageQueueBase implements IEventReceiver {
    protected final ConcurrentHashMap<UUID, CompletableFuture<Event>> requests = new ConcurrentHashMap<>();
    private final IEventSender sender;

    protected EventType[] supportedEventTypes;

    protected MessageQueueBase(IEventSender sender) {
        this.sender = sender;
    }

    protected Event sendRequestAndAwaitReponse(Object payload, EventType eventType){
        Event request = new Event(eventType.getName(), new Object[] {payload}, UUID.randomUUID());
        requests.put(request.getUUID(), new CompletableFuture<>());
        try {
            this.sender.sendEvent(request);
        } catch (Exception e) {
            throw new Error(e);
        }
        Event response = requests.get(request.getUUID()).join();
        return response;
    }

    public void receiveEvent(Event event) throws Exception {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        if (Arrays.stream(supportedEventTypes).anyMatch(eventType -> eventType.matches(event.getEventType()))) {
            CompletableFuture<Event> cf = requests.get(event.getUUID());
            if (cf != null)
                cf.complete(event);
        }

        System.out.println("--------------------------------------------------------");
    }
}
