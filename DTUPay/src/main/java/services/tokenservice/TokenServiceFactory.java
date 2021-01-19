package services.tokenservice;

import messagequeue.EventPortAdapterFactory;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.interfaces.IEventReceiver;

public class TokenServiceFactory {

    private static ITokenService service;

    public ITokenService getService() {
        if(service == null) {
            // Specific implementation of the service
            service = new MessageQueueTokenService(
                    new EventExchangeFactory().getExchange().createIEventSender()
            );
            // Registers to the EventPortAdapter
            new EventPortAdapterFactory().getPortAdapter()
                    .registerReceiver( (IEventReceiver) service);
        }
        return service;
    }
}
