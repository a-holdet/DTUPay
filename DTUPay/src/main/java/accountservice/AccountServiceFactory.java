package accountservice;

import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;

public class AccountServiceFactory {
    static IAccountService service;
    public IAccountService getService() {
        if (service == null) {
            service = new MessageQueueAccountService(
                    new EventExchangeFactory().getExchange().getSender()
            );
            new EventQueue((IEventReceiver) service).startListening();
        }
        return service;
    }
}
