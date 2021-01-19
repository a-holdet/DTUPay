package messaging.rmq.event;

import com.google.gson.Gson;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;
import messaging.rmq.RMQQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.objects.Event;

import java.nio.charset.StandardCharsets;

/***
 * @Author Simon Borup Lindegren, s185370
 */
public class EventQueue extends RMQQueue {
    // all EventQueue use same routing to the EventExchange
    public static final String ROUTING_KEY = EventExchange.ROUTING_KEY_ALL;

    static EventExchange eventExchange = new EventExchangeFactory().getExchange();
    IEventReceiver receiver;

    public EventQueue(IEventReceiver receiver) {
        super(eventExchange, ROUTING_KEY);
        this.receiver = receiver;
    }

    @Override
    public DeliverCallback getDeliverCallback() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Event event = new Gson().fromJson(message, Event.class);
            receiver.receiveEvent(event);
        };
        return deliverCallback;
    }

    @Override
    public CancelCallback getCancelCallback() {
        CancelCallback cancelCallback = (consumerTag) -> {};
        return cancelCallback;
    }
}
