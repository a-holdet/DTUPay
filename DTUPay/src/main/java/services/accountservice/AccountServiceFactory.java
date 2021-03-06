package services.accountservice;

import messagequeue.EventPortAdapterFactory;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.interfaces.IEventReceiver;

/***
 * @Author Benjamin Wrist Lam, s153486
 */

public class AccountServiceFactory {
    static IAccountService service;

    public IAccountService getService() {
        if (service == null) {
            // Specific implementation of service
            service = new MessageQueueAccountService(
                    new EventExchangeFactory().getExchange().createIEventSender()
            );
            // Registers to the EventPortAdapter
            new EventPortAdapterFactory().getPortAdapter()
                    .registerReceiver( (IEventReceiver) service);

        }
        return service;
    }
}
