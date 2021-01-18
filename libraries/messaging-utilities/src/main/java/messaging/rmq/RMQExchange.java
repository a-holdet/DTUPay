package messaging.rmq;

public abstract class RMQExchange {

    protected final RMQChannel parentChannel = RMQChannel.instance;
    protected final String exchangeName;

    public RMQExchange(String exchangeName, String exchangeType) {
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
