package messaging.rmq;

import com.google.gson.Gson;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public abstract class RMQExchange {

    public static final String FIXED_ROUTING_KEY = "route_key"; // todo delete later?
    private static final Object SYNC_LOCK = new Object();

    public final RMQChannel parentChannel = RMQChannel.instance;

    public final String exchangeName;

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

    public IEventSender getSender() {
        // new IEventSender() { @Override public void sendEvent(Event event)throws
        // Exception {} };
        return new IEventSender() {
            @Override
            public void sendEvent(Event event) throws Exception {
                String message = new Gson().toJson(event);
                synchronized (SYNC_LOCK) {
                    parentChannel.getChannel().basicPublish(exchangeName, FIXED_ROUTING_KEY, null,
                            message.getBytes("UTF-8"));
                }
            }
        };
    }

    // public IEventSender getSender(String routingKey) {
    // // new IEventSender() { @Override public void sendEvent(Event event)throws
    // Exception {} };
    // return new IEventSender() {
    // @Override
    // public void sendEvent(Event event) throws Exception {
    // String message = new Gson().toJson(event);
    // parentChannel.getChannel().basicPublish(exchangeName, routingKey, null,
    // message.getBytes("UTF-8"));
    // }
    // };
    // }
}
