package messaging.rmq.event;

import com.google.gson.Gson;
import messaging.rmq.RMQExchange;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class EventExchange extends RMQExchange {
    //could be replace by factory
    public static final EventExchange instance = new EventExchange();

    public static final String EXCHANGE_NAME = "EventExchange";
    public static final String EXCHANGE_TYPE = "topic";
    public static final String ROUTING_KEY_ALL = "#";

    public EventExchange() {
        super(EXCHANGE_NAME, EXCHANGE_TYPE);
    }

    public IEventSender getSender() {
        return this.getSender(ROUTING_KEY_ALL);
    }

    public IEventSender getSender(String routingKey) {
        return new IEventSender() {
            @Override
            public void sendEvent(Event event) {
                String message = new Gson().toJson(event);
                parentChannel.basicPublish(EXCHANGE_NAME, routingKey, message);
            }
        };
    }
}
