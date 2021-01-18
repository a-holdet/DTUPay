package messaging.rmq;

public abstract class RMQExchange {

    protected final RMQChannel parentChannel;
    protected final String exchangeName;

    public RMQExchange(RMQChannel parentChannel, String exchangeName, String exchangeType) {
        this.parentChannel = parentChannel;
        this.exchangeName = exchangeName;
        parentChannel.exchangeDeclare(exchangeName, exchangeType);
    }

    public String addQueue(String routingKey) {
        String queueName = parentChannel.queueDeclare();
        if (routingKey != null)
            parentChannel.queueBind(queueName, exchangeName, routingKey);
        return queueName;
    }
}
