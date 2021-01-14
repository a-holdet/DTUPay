package messaging.channels;

import messaging.EventReceiver;
import messaging.rabbitmq.RabbitMqListener;

public class EventServiceListener extends RabbitMqListener {

    private static final String EXCHANGE_NAME = "eventsExchange";
    private static final String QUEUE_TYPE = "topic";
    private static final String TOPIC = "events";

    public EventServiceListener(EventReceiver service) {
        super(service, QUEUE_TYPE, EXCHANGE_NAME, TOPIC);
    }
}
