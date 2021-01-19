package services.token;

import DTO.TokenCreationDTO;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import com.google.gson.reflect.TypeToken;
import messaging.rmq.event.objects.MessageQueueBase;
import messaging.rmq.event.objects.EventType;
import java.util.List;
import java.util.UUID;

public class MessageQueueTokenService extends MessageQueueBase implements IEventReceiver, ITokenService {

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
    public List<UUID> createTokens(TokenCreationDTO tokenCreationDTO) throws IllegalTokenGrantingException {
        Event responseEvent = sendRequestAndAwaitResponse(tokenCreationDTO, createTokens);

        if (responseEvent.isSuccessReponse())
            return responseEvent.getPayloadAs(new TypeToken<>() {});

        throw new IllegalTokenGrantingException(responseEvent.getErrorMessage());
    }

}
