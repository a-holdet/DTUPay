package messaging.rmq.event.objects;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MessageQueueBase implements IEventReceiver {
    protected final ConcurrentHashMap<UUID, CompletableFuture<Event>> requests = new ConcurrentHashMap<>();
    protected final IEventSender sender;

    protected MessageQueueBase(IEventSender sender) {
        this.sender = sender;
    }

    protected Event sendRequestAndAwaitResponse(Object payload, EventType eventType){
        Event request = new Event(eventType.getName(), new Object[] {payload}, UUID.randomUUID());
        requests.put(request.getUUID(), new CompletableFuture<>());
        this.sender.sendEvent(request);
        Event response = requests.get(request.getUUID()).join();
        return response;
    }

    public void receiveEvent(Event event) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        if (Arrays.stream(this.getSupportedEventTypes()).anyMatch(eventType -> eventType.matches(event.getEventType()))) {
            CompletableFuture<Event> cf = requests.remove(event.getUUID());
            if (cf != null)
                cf.complete(event);
        }

        System.out.println("--------------------------------------------------------");
    }
}
