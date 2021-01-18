package accounts;

import DTO.Customer;
import DTO.Merchant;
import messagequeuebase.MessageQueueBase;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

public class MessageQueueAccountService extends MessageQueueBase implements IMerchantService, ICustomerService{

    // Singleton as method due to serviceTest
    private static MessageQueueAccountService instance;

    public static MessageQueueAccountService getInstance() {
        if (instance == null) {
            try {
                var ies = EventExchange.instance.getSender();
                MessageQueueAccountService service = new MessageQueueAccountService(ies);
                new EventQueue(service).startListening();
                instance = service;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        return instance;
    }

    private final EventType registerMerchant = new EventType("registerMerchant");
    private final EventType getMerchant = new EventType("getMerchant");
    private final EventType registerCustomer = new EventType("registerCustomer");
    private final EventType customerExists = new EventType("customerExists");
    private final EventType getCustomer = new EventType("getCustomer");


    public MessageQueueAccountService(IEventSender sender) {
        super(sender);
        supportedEventTypes = new EventType[]{registerMerchant, getMerchant, registerCustomer, customerExists, getCustomer}; //this does not work as super constructor argument, not sure why
        instance = this; // needed for service tests!
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
