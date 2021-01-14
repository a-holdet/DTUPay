package messaging.rmq;

import java.util.Set;
import java.util.HashSet;

public abstract class RMQExchange {
    protected static RMQChannel rmqChannel = RMQChannel.instance;

    // protected final Set<RMQQueue> queues;
    protected final String exchangeName;

    public RMQExchange(String exchangeName, String exchangeType) {
        // this.queues = new HashSet<RMQQueue>();
        this.exchangeName = exchangeName;
        rmqChannel.exchangeDeclare(exchangeName, exchangeType);
    }

    public void addQueue(String queueName, String routingKey) {
        rmqChannel.queueDeclare(queueName, exchangeName);
        if (routingKey != null)
            rmqChannel.queueBind(queueName, exchangeName, routingKey);
        // queues.add();
    }
}
