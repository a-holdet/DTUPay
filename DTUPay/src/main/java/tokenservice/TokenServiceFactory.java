package tokenservice;

import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;

public class TokenServiceFactory {

    private static ITokenService service;

    public ITokenService getService() {
        if(service == null) {
            service = new MessageQueueTokenService(
                    new EventExchangeFactory().getExchange().getSender()
            );
            new EventQueue((IEventReceiver) service).startListening();
        }
        return service;
    }
}
