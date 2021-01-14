package messaging.rabbitmq;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import messaging.Event;
import messaging.EventReceiver;

public abstract class RabbitMqListener {

	private final String QUEUE_TYPE;
	private final String EXCHANGE_NAME;
	private final String BINDING;

	EventReceiver service;

	public RabbitMqListener(EventReceiver service, String queueType, String exchangeName, String binding) {
		this.QUEUE_TYPE = queueType;
		this.EXCHANGE_NAME = exchangeName;
		this.BINDING = binding;
		this.service = service;
	}

	public void listen() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("rabbitMq");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE);
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, BINDING);

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println("[x] receiving "+message);

			Event event = new Gson().fromJson(message, Event.class);
			try {
				service.receiveEvent(event);
			} catch (Exception e) {
				throw new Error(e);
			}
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}
}
