package tokenservice.MQ;

import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import messaging.rmq.MessageQueueBase;
import tokenservice.interfaces.ICustomerService;

public class MQCustomerService extends MessageQueueBase implements ICustomerService {

    private final EventType customerExistsEvent = new EventType("customerExists");

    public MQCustomerService(IEventSender sender) {
        super(sender);
        supportedEventTypes = new EventType[]{customerExistsEvent};
    }

    @Override
    public boolean customerExists(String customerId) {
        Event responseEvent = sendRequestAndAwaitReponse(customerId, customerExistsEvent);

        if (responseEvent.isSuccessReponse())
            return responseEvent.getPayloadAs(Boolean.class);

        throw new Error(responseEvent.getErrorMessage());
    }
}
