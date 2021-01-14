package messaging.rmq;

public abstract class RMQQueue {

    public final String queueName;
    public final String routingKey;

    public RMQQueue(RMQExchange rmqExchange, String queueName, String routingKey) {
        this.queueName = queueName;
        this.routingKey = routingKey;
        rmqExchange.addQueue(queueName, routingKey);
    }
}