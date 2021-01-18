package messaging.rmq.event;

import messaging.rmq.RMQChannelFactory;

public class EventExchangeFactory {
    static EventExchange exchange;
    public EventExchange getExchange() {
        if(exchange == null) {
            exchange = new EventExchange(
                    new RMQChannelFactory().getChannel()
            );
        }
        return exchange;
    }
}
