package accountservice;

import accountservice.customerservice.Customer;
import accountservice.customerservice.CustomerDoesNotExistException;
import accountservice.merchantservice.Merchant;
import accountservice.merchantservice.MerchantDoesNotExistException;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.EventServiceBase;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

public class MessageQueueAccountService extends EventServiceBase implements IAccountService {

    static final EventType registerMerchant = new EventType("registerMerchant");
    static final EventType getMerchant = new EventType("getMerchant");
    static final EventType registerCustomer = new EventType("registerCustomer");
    static final EventType customerExists = new EventType("customerExists");
    static final EventType getCustomer = new EventType("getCustomer");
    static final EventType[] supportedEventTypes =
            new EventType[]{registerMerchant, getMerchant, registerCustomer, customerExists, getCustomer};

    public MessageQueueAccountService(IEventSender sender) {
        super(sender, supportedEventTypes);
    }

    @Override
    public String registerMerchant(Merchant merchant) throws IllegalArgumentException {
        Event response = sendRequestAndAwaitReponse(merchant,registerMerchant);
        if(response.isSuccessReponse())
            return response.getPayloadAs(String.class);
        else
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    @Override
    public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
        Event response = sendRequestAndAwaitReponse(merchantId,getMerchant);
        if(response.isSuccessReponse())
            return response.getPayloadAs(Merchant.class);
        else
            throw new MerchantDoesNotExistException(response.getErrorMessage());
    }

    @Override
    public String registerCustomer(Customer customer) throws IllegalArgumentException {
        Event response = sendRequestAndAwaitReponse(customer,registerCustomer);
        if(response.isSuccessReponse())
            return response.getPayloadAs(String.class);
        else
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    @Override
    public boolean customerExists(String customerId)  {
        Event response = sendRequestAndAwaitReponse(customerId,customerExists);
        if(response.isSuccessReponse())
            return response.getPayloadAs(Boolean.class);
        else
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    @Override
    public Customer getCustomer(String customerId) throws CustomerDoesNotExistException {
        System.out.println("Creating GetCustomer event");
        Event response = sendRequestAndAwaitReponse(customerId,getCustomer);
        if(response.isSuccessReponse())
            return response.getPayloadAs(Customer.class);
        else
            throw new CustomerDoesNotExistException(response.getErrorMessage());
    }
}
