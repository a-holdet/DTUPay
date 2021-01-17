package messaging.rmq.event;

import messaging.rmq.RMQExchange;
import messaging.rmq.RMQQueue;

public class EventQueue extends RMQQueue {

    private static EventExchange eventExchange = EventExchange.instance;

    // public static final String QUEUE_NAME =
    // EventQueueEvents.class.getSimpleName();
    public static final String ROUTING_KEY = RMQExchange.FIXED_ROUTING_KEY; // delete later?

    public EventQueue() {
        super(eventExchange, ROUTING_KEY);
    }
}
