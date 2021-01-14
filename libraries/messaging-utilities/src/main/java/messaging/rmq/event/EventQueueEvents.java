package messaging.rmq.event;

import java.io.IOException;

import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.CancelCallback;
import messaging.rmq.RMQQueue;
import messaging.rmq.RMQChannel;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class EventQueueEvents extends RMQQueue {

    static EventExchange eventExchange = EventExchange.instance;
    static RMQChannel rmqChannel = RMQChannel.instance;

    public static final String QUEUE_NAME = "events_name";
    public static final String ROUTING_KEY = "events_route";

    public static final EventQueueEvents instance = new EventQueueEvents();

    public EventQueueEvents() {
        super(eventExchange, QUEUE_NAME, ROUTING_KEY);
    }

    public void registerReceiver(IEventReceiver receiver) throws Exception {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("Received Event!");
            String message = new String(delivery.getBody(), "UTF-8");
            Event event = new Gson().fromJson(message, Event.class);
            try {
                receiver.receiveEvent(event);
            } catch (Exception e) {
                throw new Error(e);
            }
            System.out
                    .println("Received Event! : " + EventExchange.EXCHANGE_NAME + ", " + ROUTING_KEY + ", " + message);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
        };

        rmqChannel.getChannel().basicConsume(queueName, true, deliverCallback, cancelCallback);
        System.out.println("registered receiver");
    }

    public IEventSender getSender() {
        System.out.println("got sender");
        return new IEventSender() {
            @Override
            public void sendEvent(Event event) throws Exception {
                String message = new Gson().toJson(event);
                rmqChannel.getChannel().basicPublish(EventExchange.EXCHANGE_NAME, ROUTING_KEY, null,
                        message.getBytes("UTF-8"));
                System.out
                        .println("Send Event! : " + EventExchange.EXCHANGE_NAME + ", " + ROUTING_KEY + ", " + message);
            }
        };
    }
}
