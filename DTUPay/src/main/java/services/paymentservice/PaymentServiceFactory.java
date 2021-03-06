package services.paymentservice;

import messagequeue.EventPortAdapterFactory;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.interfaces.IEventReceiver;

/***
 * @Author Jakob Vestergaard Offersen, s163940
 */

public class PaymentServiceFactory {
    static IPaymentService service;

    public IPaymentService getService() {
        if(service == null) {
            // Specific implementation of service
            service = new MessageQueuePaymentService(
                    new EventExchangeFactory().getExchange().createIEventSender()
            );
            // Registers to the EventPortAdapter
            new EventPortAdapterFactory().getPortAdapter()
                    .registerReceiver( (IEventReceiver) service);
        }
        return service;
    }
}
