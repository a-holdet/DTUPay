package tokenservice;

import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.interfaces.IEventReceiver;
import tokenservice.MQ.MQCustomerService;
import tokenservice.messagequeue.EventPortAdapterFactory;
import tokenservice.tokenservice.LocalTokenService;
import tokenservice.MQ.MQTokenService;
import tokenservice.tokenservice.TokenInMemoryRepository;

public class StartUp {
    public static void main(String[] args) {
        var sender = new EventExchangeFactory().getExchange().createIEventSender();
        var customerService = new MQCustomerService(sender);

        sender = new EventExchangeFactory().getExchange().createIEventSender();
        var tokenRepository = new TokenInMemoryRepository();
        var tokenService = new LocalTokenService(customerService,tokenRepository);
        var tokenEventService = new MQTokenService(sender, tokenService);

        var eventPortAdapter = new EventPortAdapterFactory().getPortAdapter();
        eventPortAdapter.registerReceiver((IEventReceiver) customerService);
        eventPortAdapter.registerReceiver((IEventReceiver) tokenEventService);
    }
}
