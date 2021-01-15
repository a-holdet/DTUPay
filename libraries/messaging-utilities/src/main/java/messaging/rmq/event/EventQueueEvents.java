package messaging.rmq.event;

import messaging.rmq.RMQQueue;

public class EventQueueEvents extends RMQQueue {

    static EventExchange eventExchange = EventExchange.instance;

    public static final String QUEUE_NAME = "events_name";
    public static final String ROUTING_KEY = "events_route";

    public static final EventQueueEvents instance = new EventQueueEvents();

    public EventQueueEvents() {
        super(eventExchange, QUEUE_NAME, ROUTING_KEY);
    }
}
