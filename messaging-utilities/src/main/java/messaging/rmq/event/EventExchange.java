package messaging.rmq.event;

import com.google.gson.Gson;
import messaging.rmq.RMQChannel;
import messaging.rmq.RMQExchange;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

import java.util.UUID;

/***
 * @Author Michael Davidsen Kirkegaard, s153587
 */
public class EventExchange extends RMQExchange {

    public static final String EXCHANGE_NAME = "EventExchange";
    public static final String EXCHANGE_TYPE = "topic";
    public static final String ROUTING_KEY_ALL = "#";

    public EventExchange(RMQChannel parentChannel) {
        super(parentChannel, EXCHANGE_NAME, EXCHANGE_TYPE);
    }

    public IEventSender createIEventSender() {
        return this.createIEventSender(ROUTING_KEY_ALL);
    }

    public IEventSender createIEventSender(String routingKey) {
        return new IEventSender() {
            @Override
            public void sendEvent(Event event) {
                String message = new Gson().toJson(event);
                parentChannel.basicPublish(EXCHANGE_NAME, routingKey, message);
            }
        };
    }
}
