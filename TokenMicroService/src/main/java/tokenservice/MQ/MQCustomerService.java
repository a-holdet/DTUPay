package tokenservice.MQ;

import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import messaging.rmq.event.objects.EventServiceBase;
import tokenservice.interfaces.ICustomerService;

public class MQCustomerService extends EventServiceBase implements ICustomerService {

    private static final EventType customerExistsEvent = new EventType("customerExists");
    private static final EventType[] supportedEventTypes = new EventType[]{customerExistsEvent};

    public MQCustomerService(IEventSender sender) {
        super(sender);
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return supportedEventTypes;
    }

    @Override
    public boolean customerExists(String customerId) {
        Event responseEvent = sendRequestAndAwaitReponse(customerId, customerExistsEvent);

        if (responseEvent.isSuccessReponse())
            return responseEvent.getPayloadAs(Boolean.class);

        throw new Error(responseEvent.getErrorMessage());
    }
}
