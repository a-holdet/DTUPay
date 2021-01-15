package messaging.rmq;

public abstract class RMQExchange {

    public final RMQChannel parentChannel = RMQChannel.instance;

    public final String exchangeName;

    public RMQExchange(String exchangeName, String exchangeType) {
        this.exchangeName = exchangeName;
        parentChannel.exchangeDeclare(exchangeName, exchangeType);
    }

    public void addQueue(String queueName, String routingKey) {
        parentChannel.queueDeclare(queueName);
        if (routingKey != null)
            parentChannel.queueBind(queueName, exchangeName, routingKey);
    }
}
