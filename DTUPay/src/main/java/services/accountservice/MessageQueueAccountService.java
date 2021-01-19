package services.accountservice;

import DTO.Customer;
import DTO.Merchant;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.MessageQueueBase;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

public class MessageQueueAccountService extends MessageQueueBase implements IAccountService {

    static final EventType registerMerchant = new EventType("registerMerchant");
    static final EventType getMerchant = new EventType("getMerchant");
    static final EventType registerCustomer = new EventType("registerCustomer");
    static final EventType customerExists = new EventType("customerExists");
    static final EventType getCustomer = new EventType("getCustomer");
    static final EventType[] supportedEventTypes =
            new EventType[]{registerMerchant, getMerchant, registerCustomer, customerExists, getCustomer};

    public MessageQueueAccountService(IEventSender sender) {
        super(sender);
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return supportedEventTypes;
    }

    @Override
    public String registerMerchant(Merchant merchant) throws IllegalArgumentException {
        Event response = sendRequestAndAwaitResponse(merchant,registerMerchant);
        if(response.isSuccessReponse())
            return response.getPayloadAs(String.class);
        else
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    @Override
    public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
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
}
