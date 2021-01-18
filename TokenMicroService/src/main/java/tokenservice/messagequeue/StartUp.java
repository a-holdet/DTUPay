package tokenservice.messagequeue;


import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import tokenservice.ITokenService;
import tokenservice.LocalTokenService;
import tokenservice.customer.ICustomerService;
import tokenservice.customer.MessageQueueCustomerService;

public class StartUp {
    public static void main(String[] args) {
        var ies = EventExchange.instance.getSender();

        ICustomerService customerService = new MessageQueueCustomerService(ies);
        ITokenService tokenService = new LocalTokenService(customerService);
        EventService eventService = new EventService(ies, tokenService);
        new EventQueue(eventService).startListening();
        new EventQueue((IEventReceiver) customerService).startListening();
    }
}
