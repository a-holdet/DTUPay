package tokenservice;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import tokenservice.MQ.MQCustomerService;
import tokenservice.tokenservice.LocalTokenService;
import tokenservice.MQ.MQTokenService;
import tokenservice.tokenservice.TokenInMemoryRepository;

public class StartUp {
    public static void main(String[] args) {

        var sender = EventExchange.instance.getSender();
        var customerService = new MQCustomerService(sender);
        var tokenRepository = new TokenInMemoryRepository();
        var tokenService = new LocalTokenService(customerService,tokenRepository);
        var tokenEventService = new MQTokenService(sender, tokenService);

        new EventQueue(tokenEventService).startListening();
        new EventQueue(customerService).startListening();
    }
}
