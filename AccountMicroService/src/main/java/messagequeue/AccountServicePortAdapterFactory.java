package messagequeue;

import customerservice.CustomerServiceFactory;
import merchantservice.MerchantServiceFactory;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;

public class AccountServicePortAdapterFactory {
    static IEventReceiver service;
    public IEventReceiver getPortAdapter() {
        if(service == null) {
            // Specific implementation of the port adapter
            service = new AccountServicePortAdapter(
                    new EventExchangeFactory().getExchange().createIEventSender(),
                    new MerchantServiceFactory().getService(),
                    new CustomerServiceFactory().getService()
            );
            // Registers with new queue
            new EventQueue(service).startListening();
        }
        return service;
    }
}
