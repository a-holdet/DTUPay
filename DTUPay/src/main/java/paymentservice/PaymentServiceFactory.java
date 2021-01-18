package paymentservice;

import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;

public class PaymentServiceFactory {
    static IPaymentService service;

    public IPaymentService getService() {
        if(service == null) {
            service = new MessageQueuePaymentService(
                    new EventExchangeFactory().getExchange().getSender()
            );
            new EventQueue((IEventReceiver) service).startListening();
        }
        return service;
    }
}
