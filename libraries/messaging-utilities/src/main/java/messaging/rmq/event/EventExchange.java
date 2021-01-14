package messaging.rmq.event;

import messaging.rmq.RMQChannel;
import messaging.rmq.RMQExchange;

public class EventExchange extends RMQExchange {

    public static final EventExchange instance = new EventExchange();

    static RMQChannel rmqChannel = RMQChannel.instance;

    public static final String EXCHANGE_NAME = "EventExchange";
    public static final String EXCHANGE_TYPE = "topic";

    public EventExchange() {
        super(EXCHANGE_NAME, EXCHANGE_TYPE);
    }
}
