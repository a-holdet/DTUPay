package messagequeue;

import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventSender;
import accountservice.MessageQueueCustomerService;
import tokenservice.ITokenRepository;
import tokenservice.LocalTokenService;
import tokenservice.TokenInMemoryRepository;

/***
 * @Author Christian Dan Hjelmslund, s164412
 */

public class StartUp {
    public static void main(String[] args) {
        IEventSender eventSender1 = new EventExchangeFactory().getExchange().createIEventSender();
        IEventSender eventSender2 = new EventExchangeFactory().getExchange().createIEventSender();

        ITokenRepository tokenRepository = new TokenInMemoryRepository();

        var customerService = new MessageQueueCustomerService(eventSender1);
        var tokenService = new LocalTokenService(customerService,tokenRepository);
        var tokenEventService = new MessageQueueConnector(eventSender2, tokenService);

        var eventPortAdapter = new EventPortAdapter();

        eventPortAdapter.registerReceiver(customerService);
        eventPortAdapter.registerReceiver(tokenEventService);

        new EventQueue(eventPortAdapter).startListening();
    }
}
