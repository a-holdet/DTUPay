package messaging.rmq.event;

import messaging.rmq.RMQChannelFactory;

/***
 * @Author Michael Davidsen Kirkegaard, s153587
 */
public class EventExchangeFactory {
    static EventExchange exchange;
    public EventExchange getExchange() {
        if (exchange == null) {
            exchange = new EventExchange(new RMQChannelFactory().getChannel());
        }
        return exchange;
    }
}
