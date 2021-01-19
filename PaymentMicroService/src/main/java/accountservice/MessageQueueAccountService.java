package accountservice;

/***
 * @Author Jakob Vestergaard Offersen, s163940
 */

import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import messaging.rmq.event.objects.MessageQueueBase;


public class MessageQueueAccountService extends MessageQueueBase implements IMerchantService, ICustomerService {

    private final EventType getMerchant = new EventType("getMerchant");
    private final EventType getCustomer = new EventType("getCustomer");

    public MessageQueueAccountService(IEventSender sender) { super(sender); }

    @Override
    public EventType[] getSupportedEventTypes() {
        return new EventType[] {getMerchant, getCustomer};
    }

    @Override
    public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
        Event response = sendRequestAndAwaitResponse(merchantId, getMerchant);

        if (response.isFailureReponse())
            throw new MerchantDoesNotExistException(response.getErrorMessage());
        else
            return response.getPayloadAs(Merchant.class);

    }

    @Override
    public Customer getCustomer(String customerId) throws CustomerDoesNotExistException {
        Event response = sendRequestAndAwaitResponse(customerId, getCustomer);

        if (response.isFailureReponse())
            throw new CustomerDoesNotExistException(response.getErrorMessage());
        else
            return response.getPayloadAs(Customer.class);
    }
}
