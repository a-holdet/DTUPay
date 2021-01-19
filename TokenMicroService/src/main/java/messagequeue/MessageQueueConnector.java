package messagequeue;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import accountservice.CustomerNotFoundException;
import tokenservice.ITokenService;
import tokenservice.IllegalTokenGrantingException;
import tokenservice.TokenDoesNotExistException;
import tokenservice.TokenCreationDTO;

import java.util.List;
import java.util.UUID;

public class MessageQueueConnector implements IEventReceiver {

    private final IEventSender sender;
    private final ITokenService tokenService;

    private final EventType consumeToken = new EventType("consumeToken");
    private final EventType createTokens = new EventType("createTokens");

    public MessageQueueConnector(IEventSender sender, ITokenService tokenService) {
        this.sender = sender;
        this.tokenService = tokenService;
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return new EventType[] {consumeToken, createTokens};
    }

    @Override
    public void receiveEvent(Event event) {
        if (event.getEventType().equals(consumeToken.getName())) {
            UUID customerToken = event.getArgument(0, UUID.class);
            consumeToken(customerToken, event.getUUID());
        } else if (event.getEventType().equals(createTokens.getName())) {
            TokenCreationDTO tokenCreationDTO = event.getArgument(0, TokenCreationDTO.class);
            createTokensForCustomer(tokenCreationDTO, event.getUUID());
        }
    }

    private void createTokensForCustomer(TokenCreationDTO tokenCreationDTO, UUID eventID) {
        try {
            List<UUID> tokens = tokenService.createTokensForCustomer(tokenCreationDTO.getUserId(), tokenCreationDTO.getTokenAmount());
            Event createTokensResponse = new Event(createTokens.succeeded(), new Object[]{tokens}, eventID);
            sender.sendEvent(createTokensResponse);
        } catch (IllegalTokenGrantingException | CustomerNotFoundException e) {
            sender.sendEvent(Event.GetFailedEvent(createTokens, e, eventID));
        }
    }

    private void consumeToken(UUID customerToken, UUID eventID) {
        try {
            String customerId = tokenService.consumeToken(customerToken);
            sender.sendEvent(new Event(consumeToken.succeeded(), new Object[]{customerId}, eventID));
        } catch (TokenDoesNotExistException e) {
            sender.sendEvent(Event.GetFailedEvent(consumeToken, e, eventID));
        }
    }

}
