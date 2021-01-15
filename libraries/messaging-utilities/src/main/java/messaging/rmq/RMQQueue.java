package messaging.rmq;

import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.CancelCallback;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public abstract class RMQQueue {

    public final String queueName;
    public final String routingKey;
    public final RMQExchange parentExchange;

    public RMQQueue(RMQExchange parentExchange, String queueName, String routingKey) {
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.parentExchange = parentExchange;
        parentExchange.addQueue(queueName, routingKey);
    }

    public void registerReceiver(IEventReceiver receiver) throws Exception {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            Event event = new Gson().fromJson(message, Event.class);
            try {
                receiver.receiveEvent(event);
            } catch (Exception e) {
                throw new Error(e);
            }
        };

        CancelCallback cancelCallback = (consumerTag) -> {};

        parentExchange.parentChannel.getChannel().basicConsume(queueName, true, deliverCallback, cancelCallback);
    }

    public IEventSender getSender() {
        return event -> {
            String message = new Gson().toJson(event);
            parentExchange.parentChannel.getChannel().basicPublish(parentExchange.exchangeName, routingKey, null,
                    message.getBytes("UTF-8"));
        };
    }
}