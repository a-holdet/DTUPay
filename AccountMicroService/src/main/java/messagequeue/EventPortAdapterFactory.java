package messagequeue;

import customerservice.CustomerServiceFactory;
import merchantservice.MerchantServiceFactory;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;

public class EventPortAdapterFactory {
    static IEventReceiver service;
    public IEventReceiver getPortAdapter() {
        if(service == null) {
            service = new EventPortAdapter(
                    new MerchantServiceFactory().getService(),
                    new CustomerServiceFactory().getService(),
                    new EventExchangeFactory().getExchange().getSender()
            );
            new EventQueue(service).startListening();
        }
        return service;
    }
}
