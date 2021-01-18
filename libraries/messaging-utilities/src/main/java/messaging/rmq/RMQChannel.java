package messaging.rmq;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.*;

public class RMQChannel {

    private final Channel consumerChannel;
    private final Channel producerChannel;

    public RMQChannel() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitMq");
        try {
            Connection producerConnection = factory.newConnection();
            Connection consumerConnection = factory.newConnection();
            this.consumerChannel = producerConnection.createChannel();
            this.producerChannel = consumerConnection.createChannel();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public void exchangeDeclare(String exchangeName, String exchangeType) {
        try {
            this.producerChannel.exchangeDeclare(exchangeName, exchangeType);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public String queueDeclare() {
        String queueName;
        try {
            queueName = consumerChannel.queueDeclare().getQueue();
        } catch (IOException e) {
            throw new Error(e);
        }
        return queueName;
    }

    public void queueBind(String queueName, String exchangeName, String routingKey) {
        try {
            consumerChannel.queueBind(queueName, exchangeName, routingKey);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    private static final Object SYNC_LOCK_PUBLISH = new Object();
    public void basicPublish(String exchangeName, String routingKey, String message) {
        synchronized (SYNC_LOCK_PUBLISH) {
            try {
                producerChannel.basicPublish(
                        exchangeName,
                        routingKey,
                        null,
                        message.getBytes(StandardCharsets.UTF_8)
                );
            } catch (IOException e) {
                throw new Error(e);
            }
        }
    }

    public void basicConsume(String queueName, DeliverCallback deliverCallback, CancelCallback cancelCallback) {
        try {
            consumerChannel.basicConsume(
                queueName,
                true,
                deliverCallback,
                cancelCallback
            );
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
