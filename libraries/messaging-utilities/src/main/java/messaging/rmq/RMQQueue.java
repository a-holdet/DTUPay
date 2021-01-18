package messaging.rmq;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.CancelCallback;

public abstract class RMQQueue {

    protected final RMQExchange parentExchange;
    public final String queueName;
    public final String routingKey;

    public RMQQueue(RMQExchange parentExchange, String routingKey) {
        this.parentExchange = parentExchange;
        this.queueName = parentExchange.addQueue(routingKey);
        this.routingKey = routingKey;
    }

    public abstract DeliverCallback getDeliverCallback();
    public abstract CancelCallback getCancelCallback();

    public void startListening() {
        parentExchange.parentChannel.basicConsume(queueName, getDeliverCallback(), getCancelCallback());
    }
}