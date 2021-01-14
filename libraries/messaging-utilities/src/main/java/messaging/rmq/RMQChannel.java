package messaging.rmq;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RMQChannel {
    public static final RMQChannel instance = new RMQChannel();

    // Set<String> exchances;
    private final Channel channel;

    public RMQChannel() {
        // this.exchances = new HashSet<String>();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitMq");
        try {
            Connection connection = factory.newConnection();
            this.channel = connection.createChannel();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public void exchangeDeclare(String exchangeName, String exchangeType) {
        try {
            this.channel.exchangeDeclare(exchangeName, exchangeType);
            // this.exchances.add(exchangeName);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public void queueDeclare(String queueName, String exchanceName) {
        try {
            channel.queueDeclare(queueName, true, false, false, null);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public void queueBind(String queueName, String exchanceName, String routingKey) {
        try {
            channel.queueBind(queueName, exchanceName, routingKey);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public Channel getChannel() {
        return channel;
    }

}
