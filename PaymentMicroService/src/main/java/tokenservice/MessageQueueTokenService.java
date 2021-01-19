package tokenservice;

import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.MessageQueueBase;
import messaging.rmq.event.objects.EventType;
import java.util.UUID;

public class MessageQueueTokenService extends MessageQueueBase implements ITokenService {

    private final EventType consumeToken = new EventType("consumeToken");

    public MessageQueueTokenService(IEventSender sender) {
        super(sender);
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return new EventType[]{consumeToken};
    }

    @Override
    public String consumeToken(UUID customerToken) throws TokenDoesNotExistException {
        Event response = sendRequestAndAwaitResponse(customerToken, consumeToken);

        if (response.isFailureReponse()) {
            throw new TokenDoesNotExistException(response.getErrorMessage());
        } else {
            return response.getPayloadAs(String.class);
        }
    }
}
