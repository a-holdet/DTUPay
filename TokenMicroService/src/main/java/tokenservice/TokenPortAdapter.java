package tokenservice;

import io.cucumber.java.an.E;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TokenPortAdapter implements IEventReceiver {

    public static TokenPortAdapter instance = null; // startUp(); //cannot be used until after startUp();
    IEventSender sender;
    private CompletableFuture<Boolean> customerExistsResult;
    ITokenService tokenService = TokenService.instance;

    public TokenPortAdapter(IEventSender sender) {
        this.sender = sender;
    }

    public static void startUp() {
        if (instance == null) {
            try {
                var s = EventExchange.instance.getSender();
                TokenPortAdapter portAdapter = new TokenPortAdapter(s);
                new EventQueue().registerReceiver(portAdapter);
                instance = portAdapter;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }


    @Override
    public void receiveEvent(Event event) throws Exception {
        if (event.getEventType().equals("customerExistsResponse")) {
            customerExistsResponse(event);
        } else if (event.getEventType().equals("consumeToken")) {
            consumeToken(event);
        }
    }

    private void consumeToken(Event event) throws Exception {
        UUID customerToken = UUID.fromString((String) event.getArguments()[0]);

        try {
            System.out.println("tokenservice " + tokenService);
            String customerId = tokenService.consumeToken(customerToken);
            sender.sendEvent(new Event("consumeTokenResponse", new Object[]{customerId}));
            System.out.println("returned tokens for customer " + customerId);
        } catch (TokenDoesNotExistException e) {
            System.out.println("failed " + e.getMessage());
            sender.sendEvent(new Event("consumeTokenResponseFail", new Object[]{e}));
            e.printStackTrace();
        }

    }

    private void customerExistsResponse(Event event) {

        boolean customerExists = (boolean) event.getArguments()[0];
        customerExistsResult.complete(customerExists);
    }

    public boolean customerExists(String customerId) {
        try {
            customerExistsResult = new CompletableFuture<>();
            if (customerId == null) return false;
            sender.sendEvent(new Event("customerExists", new Object[]{customerId}));
            return customerExistsResult.join(); // Blocking
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
