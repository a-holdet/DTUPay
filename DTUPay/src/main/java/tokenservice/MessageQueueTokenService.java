package tokenservice;

import DTO.TokenCreation;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import com.google.gson.reflect.TypeToken;
import messaging.rmq.event.objects.EventServiceBase;
import messaging.rmq.event.objects.EventType;
import java.util.List;
import java.util.UUID;

public class MessageQueueTokenService extends EventServiceBase implements IEventReceiver, ITokenService {

    private static final EventType createTokens = new EventType("createTokens");

    private static final EventType[] supportedEventTypes = {createTokens};

    public MessageQueueTokenService(IEventSender sender) {
        super(sender);
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return supportedEventTypes;
    }

    @Override
    public List<UUID> createTokens(TokenCreation tokenCreation) throws IllegalTokenGrantingException {
        Event responseEvent = sendRequestAndAwaitReponse(tokenCreation, createTokens);

        if (responseEvent.isSuccessReponse())
            return responseEvent.getPayloadAs(new TypeToken<>() {});

        throw new IllegalTokenGrantingException(responseEvent.getErrorMessage());
    }

}
