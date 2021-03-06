package accountservice;

/***
 * @Author Jakob Vestergaard Offersen, s163940
 */
import messaging.rmq.event.objects.EventType;
import messaging.rmq.event.objects.MessageQueueBase;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class MessageQueueAccountService extends MessageQueueBase implements IAccountService {

    private final EventType registerMerchant = new EventType("registerMerchant");
    private final EventType getMerchant = new EventType("getMerchant");
    private final EventType registerCustomer = new EventType("registerCustomer");
    private final EventType customerExists = new EventType("customerExists");
    private final EventType getCustomer = new EventType("getCustomer");
    private final EventType[] supportedEventTypes = new EventType[]{registerMerchant, getMerchant, registerCustomer, customerExists, getCustomer};

    public MessageQueueAccountService(IEventSender sender) {
        super(sender);
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return supportedEventTypes;
    }

    @Override
    public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
        if (merchantId == null)
            throw new MerchantDoesNotExistException("The merchant does not exists in DTUPay");
        Event response = sendRequestAndAwaitResponse(merchantId,getMerchant);

        if(response.isSuccessReponse())
            return response.getPayloadAs(Merchant.class);
        else
            throw new MerchantDoesNotExistException(response.getErrorMessage());
    }

    @Override
    public String registerCustomer(Customer customer) throws IllegalArgumentException {
        Event response = sendRequestAndAwaitResponse(customer,registerCustomer);
        if(response.isSuccessReponse())
            return response.getPayloadAs(String.class);
        else
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    @Override
    public Customer getCustomer(String customerId) throws CustomerDoesNotExistException {
        if (customerId == null)
            throw new CustomerDoesNotExistException("The customer does not exists in DTUPay");
        Event response = sendRequestAndAwaitResponse(customerId,getCustomer);
        if(response.isSuccessReponse())
            return response.getPayloadAs(Customer.class);
        else
            throw new CustomerDoesNotExistException(response.getErrorMessage());
    }
}
