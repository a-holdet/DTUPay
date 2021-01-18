package tokenservice.messagequeue;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import tokenservice.*;
import tokenservice.customer.CustomerNotFoundException;
import tokenservice.customer.ICustomerService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class EventService implements IEventReceiver {

//    private CompletableFuture<Boolean> customerExistsResult;
    private IEventSender sender;
    private final ITokenService tokenService;

    public EventService(IEventSender sender, ITokenService tokenService) {
        this.sender = sender;
        this.tokenService = tokenService;
    }

    @Override
    public void receiveEvent(Event event) {
        System.out.println("eventType: " + event.getEventType());
        if (event.getEventType().equals("consumeToken")) {
            consumeToken(event);
        } else if (event.getEventType().equals("createTokensForCustomer")) {
            new Thread(() -> createTokensForCustomer(event)).start();
        }
    }

    private void createTokensForCustomer(Event event) {
        String customerId = event.getArgument(0, String.class);
        int amount = event.getArgument(1, int.class);

        try {
//            customerExistsResult = new CompletableFuture<>();
            List<UUID> tokens = tokenService.createTokensForCustomer(customerId, amount);
            Event createTokensResponse = new Event("createTokensForCustomerSuccess", new Object[]{tokens});
            sender.sendEvent(createTokensResponse);
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
            Event customerNotFoundEvent = new Event("createTokensForCustomerFail",
                    new Object[]{new CustomerNotFoundException("Customer was not found")});
            sender.sendEvent(customerNotFoundEvent);
        } catch (Exception e) {
            throw new Error(e.getMessage());
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
}
