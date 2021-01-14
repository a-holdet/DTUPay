package messaging.rmq;

import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.CancelCallback;
import messaging.rmq.RMQQueue;
import messaging.rmq.RMQChannel;
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
            // System.out.println("Received Event!");
            String message = new String(delivery.getBody(), "UTF-8");
            Event event = new Gson().fromJson(message, Event.class);
            try {
                receiver.receiveEvent(event);
            } catch (Exception e) {
                throw new Error(e);
            }
            // System.out.println("Received Event! : " + parentExchange.exchangeName + ", "
            // + routingKey + ", " + message);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
        };

        parentExchange.parentChannel.getChannel().basicConsume(queueName, true, deliverCallback, cancelCallback);
        // System.out.println("registered receiver");
    }

    public IEventSender getSender() {
        // System.out.println("got sender");
        return new IEventSender() {
            @Override
            public void sendEvent(Event event) throws Exception {
                String message = new Gson().toJson(event);
                parentExchange.parentChannel.getChannel().basicPublish(parentExchange.exchangeName, routingKey, null,
                        message.getBytes("UTF-8"));
                // System.out.println("Send Event! : " + EventExchange.EXCHANGE_NAME + ", " +
                // routingKey + ", " + message);
            }
        };
    }
}