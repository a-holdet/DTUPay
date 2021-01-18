package tokenservice.messagequeue;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import tokenservice.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TokenPortAdapter implements IEventReceiver {

    private CompletableFuture<Boolean> customerExistsResult;
    private IEventSender sender;
    static final ITokenService tokenService = TokenService.instance;

    public TokenPortAdapter(IEventSender sender) { this.sender = sender; }

    private static TokenPortAdapter instance;
    public static TokenPortAdapter getInstance() {
        if (instance == null) {
            try {
                var ies = new EventExchangeFactory().getExchange().getSender();
                TokenPortAdapter service = new TokenPortAdapter(ies);
                new EventQueue(service).startListening();
                instance = service;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        return instance;
    }

    @Override
    public void receiveEvent(Event event) {
        if (event.getEventType().equals("customerExistsSuccess")) {
            customerExistsResponse(event);
        } else if (event.getEventType().equals("consumeToken")) {
            consumeToken(event);
        } else if (event.getEventType().equals("createTokensForCustomer")) {
            new Thread(() -> createTokensForCustomer(event)).start();
        }
    }

    private void createTokensForCustomer(Event event) {
        String customerId = event.getArgument(0, String.class);
        int amount = event.getArgument(1, int.class);

        try {
            customerExistsResult = new CompletableFuture<>();
            sender.sendEvent(new Event("customerExists", new Object[]{customerId}));
            boolean customerExists = customerExistsResult.join();
            if (!customerExists) {
                Event customerNotFoundEvent = new Event("createTokensForCustomerFail",
                        new Object[]{new CustomerNotFoundException("Customer was not found")}, event.getUUID());
                sender.sendEvent(customerNotFoundEvent);
            } else {
                List<UUID> tokens = tokenService.createTokensForCustomer(customerId, amount);
                Event createTokensResponse = new Event("createTokensForCustomerSuccess", new Object[]{tokens});
                sender.sendEvent(createTokensResponse);
            }
        } catch (IllegalTokenGrantingException e) {
            String errorType = e.getClass().getSimpleName();
            String errorMessage = e.getMessage();
            Event createTokensResponse = new Event("createTokensForCustomerFail", new Object[]{errorType, errorMessage}, event.getUUID());
            try {
                sender.sendEvent(createTokensResponse);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
        } catch (CustomerNotFoundException e) {
            e.printStackTrace();
            // TODO: Handle this
        } catch (Exception e) {
            // TODO: throw error
            e.printStackTrace();
        }
    }

    private void consumeToken(Event event) {
        UUID customerToken = UUID.fromString(event.getArgument(0, String.class));

        try {
            String customerId = tokenService.consumeToken(customerToken);
            sender.sendEvent(new Event("consumeTokenSuccess", new Object[]{customerId}, event.getUUID()));
            System.out.println("returned tokens for customer " + customerId);
        } catch (TokenDoesNotExistException e) {
            System.out.println("Failed returned tokens for customer");
            String errorType = e.getClass().getSimpleName();
            String errorMessage = e.getMessage();
            sender.sendEvent(new Event("consumeTokenFail", new Object[]{errorType, errorMessage}, event.getUUID()));
        }
    }

    private void customerExistsResponse(Event event) {
        boolean customerExists = event.getArgument(0, Boolean.class);
        customerExistsResult.complete(customerExists);
    }

}
