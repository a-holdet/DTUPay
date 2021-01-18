package tokenservice.MQ;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import tokenservice.exceptions.CustomerNotFoundException;
import tokenservice.interfaces.ITokenService;
import tokenservice.exceptions.IllegalTokenGrantingException;
import tokenservice.exceptions.TokenDoesNotExistException;

import java.util.List;
import java.util.UUID;

public class MQTokenService implements IEventReceiver {

    private final IEventSender sender;
    private final ITokenService tokenService;
    private final EventType consumeToken = new EventType("consumeToken");
    private final EventType createTokensForCustomer = new EventType("createTokensForCustomer");

    public MQTokenService(IEventSender sender, ITokenService tokenService) {
        this.sender = sender;
        this.tokenService = tokenService;
    }

    @Override
    public void receiveEvent(Event event) {
        if (event.getEventType().equals(consumeToken.getName())) {
            consumeToken(event);
        } else if (event.getEventType().equals(createTokensForCustomer.getName())) {
            new Thread(() -> createTokensForCustomer(event)).start();
        }
    }

    private void createTokensForCustomer(Event event) {
        Event createTokensResponse;

        String customerId = event.getArgument(0, String.class);
        int amount = event.getArgument(1, int.class);

        try {
            List<UUID> tokens = tokenService.createTokensForCustomer(customerId, amount);
            createTokensResponse = new Event(createTokensForCustomer.succeeded(), new Object[]{tokens}, event.getUUID());
            sender.sendEvent(createTokensResponse);
        } catch (IllegalTokenGrantingException | CustomerNotFoundException e) {
            String errorType = e.getClass().getSimpleName();
            String errorMessage = e.getMessage();
            createTokensResponse = new Event(createTokensForCustomer.failed(), new Object[]{errorType, errorMessage}, event.getUUID());
            sender.sendEvent(createTokensResponse);
        }
    }

    private void consumeToken(Event event) {
        UUID customerToken = UUID.fromString(event.getArgument(0, String.class));

        try {
            String customerId = tokenService.consumeToken(customerToken);
            sender.sendEvent(new Event(consumeToken.succeeded(), new Object[]{customerId}, event.getUUID()));
        } catch (TokenDoesNotExistException e) {
            String errorType = e.getClass().getSimpleName();
            String errorMessage = e.getMessage();
            sender.sendEvent(new Event(consumeToken.failed(), new Object[]{errorType, errorMessage}, event.getUUID()));
        }
    }

}
