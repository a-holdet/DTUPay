package messaging.rmq;

import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RMQChannel {
    public static final RMQChannel instance = new RMQChannel();

    private final Channel channel;

    public RMQChannel() {
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
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public String queueDeclare() {
        String queueName;
        try {
            queueName = channel.queueDeclare().getQueue(); // queueName, true, false, false, null
        } catch (IOException e) {
            throw new Error(e);
        }
        return queueName;
    }

    public void queueBind(String queueName, String exchangeName, String routingKey) {
        try {
            channel.queueBind(queueName, exchangeName, routingKey);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public Channel getChannel() {
        return channel;
    }

}
