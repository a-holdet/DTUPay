package messaging.rmq;

import java.util.Set;
import java.util.HashSet;

import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.CancelCallback;
import messaging.rmq.RMQQueue;
import messaging.rmq.RMQChannel;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public abstract class RMQExchange {

    public final RMQChannel parentChannel = RMQChannel.instance;

    // protected final Set<RMQQueue> queues;
    public final String exchangeName;

    public RMQExchange(String exchangeName, String exchangeType) {
        // this.queues = new HashSet<RMQQueue>();
        this.exchangeName = exchangeName;
        parentChannel.exchangeDeclare(exchangeName, exchangeType);
    }

    public void addQueue(String queueName, String routingKey) {
        parentChannel.queueDeclare(queueName, exchangeName);
        if (routingKey != null)
            parentChannel.queueBind(queueName, exchangeName, routingKey);
        // queues.add();
    }

    public IEventSender getSender(String routingKey) {
        // System.out.println("got sender");
        return new IEventSender() {
            @Override
            public void sendEvent(Event event) throws Exception {
                String message = new Gson().toJson(event);
                parentChannel.getChannel().basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
                // System.out.println("Send Event! : " + EventExchange.EXCHANGE_NAME + ", " +
                // routingKey + ", " + message);
            }
        };
    }
}
