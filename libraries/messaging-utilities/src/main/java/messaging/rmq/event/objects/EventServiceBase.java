package messaging.rmq.event.objects;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class EventServiceBase implements IEventReceiver {
    protected final ConcurrentHashMap<UUID, CompletableFuture<Event>> requests = new ConcurrentHashMap<>();
    protected final IEventSender sender; //TODO could be changed to private once subclasses uses it properly

    protected final EventType[] supportedEventTypes;

    protected EventServiceBase(IEventSender sender, EventType[] supportedEventTypes) {
        this.sender = sender;
        this.supportedEventTypes = supportedEventTypes;
    }

    protected Event sendRequestAndAwaitReponse(Object payload, EventType eventType){
        Event request = new Event(eventType.getName(), new Object[] {payload}, UUID.randomUUID());
        requests.put(request.getUUID(), new CompletableFuture<>());
        this.sender.sendEvent(request);
        Event response = requests.get(request.getUUID()).join();
        return response;
    }

    @Override
    public void receiveEvent(Event event) {
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
