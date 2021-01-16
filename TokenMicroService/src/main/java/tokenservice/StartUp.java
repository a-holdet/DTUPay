package tokenservice;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventSender;

public class StartUp {
    public static void main(String[] args) {
        IEventSender sender = EventExchange.instance.getSender();
        TokenService tokenService = new TokenService();
        TokenPortAdapter tokenPortAdapter = new TokenPortAdapter(tokenService, sender);
        try {
            new EventQueue().registerReceiver(tokenPortAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
