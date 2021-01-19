package accountservice;

import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import messaging.rmq.event.objects.MessageQueueBase;

/***
 * @Author Sebastian Arcos Specht, s164394
 */

public class MessageQueueCustomerService extends MessageQueueBase implements ICustomerService {

    private static final EventType customerExistsEvent = new EventType("customerExists");

    public MessageQueueCustomerService(IEventSender sender) {
        super(sender);
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return new EventType[] {customerExistsEvent};
    }

    @Override
    public boolean customerExists(String customerId) {
        Event responseEvent = sendRequestAndAwaitResponse(customerId, customerExistsEvent);

        if (responseEvent.isSuccessReponse())
            return responseEvent.getPayloadAs(Boolean.class);

        throw new Error(responseEvent.getErrorMessage());
    }
}
