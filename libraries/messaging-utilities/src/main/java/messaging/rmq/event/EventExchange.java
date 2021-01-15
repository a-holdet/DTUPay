package messaging.rmq.event;

import messaging.rmq.RMQExchange;

public class EventExchange extends RMQExchange {

    public static final EventExchange instance = new EventExchange();

    public static final String EXCHANGE_NAME = "event";
    public static final String EXCHANGE_TYPE = "topic";

    public EventExchange() {
        super(EXCHANGE_NAME, EXCHANGE_TYPE);
    }
}
