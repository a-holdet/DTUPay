package tokenservice.customer;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import tokenservice.messagequeue.EventType;
import tokenservice.messagequeue.MessageQueueBase;

public class MessageQueueCustomerService extends MessageQueueBase implements ICustomerService {

    private final EventType customerExistsEvent = new EventType("customerExists");

    public MessageQueueCustomerService(IEventSender sender) {
        super(sender);
        supportedEventTypes = new EventType[]{customerExistsEvent};
    }

    @Override
    public boolean customerExists(String customerId) {
        System.out.println("før ");
        Event responseEvent = sendRequestAndAwaitReponse(customerId, customerExistsEvent);
        System.out.println("efter " + responseEvent);

        if (responseEvent.isSuccessReponse())
            return responseEvent.getPayloadAs(Boolean.class);

        throw new Error(responseEvent.getErrorMessage());
    }
}
