package accountservice;

import messaging.rmq.event.objects.EventType;
import messaging.rmq.event.objects.EventServiceBase;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class MessageQueueAccountService extends EventServiceBase implements IMerchantService, ICustomerService{

    // Singleton as method due to serviceTest
    private static MessageQueueAccountService instance;

    public static MessageQueueAccountService getInstance() {
        if (instance == null) {
            try {
                var ies = new EventExchangeFactory().getExchange().getSender();
                MessageQueueAccountService service = new MessageQueueAccountService(ies);
                new EventQueue(service).startListening();
                instance = service;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        return instance;
    }

    private static final EventType registerMerchant = new EventType("registerMerchant");
    private static final EventType getMerchant = new EventType("getMerchant");
    private static final EventType registerCustomer = new EventType("registerCustomer");
    private static final EventType customerExists = new EventType("customerExists");
    private static final EventType getCustomer = new EventType("getCustomer");
    private static final EventType[] supportedEventTypes =
            new EventType[]{registerMerchant, getMerchant, registerCustomer, customerExists, getCustomer};

    public MessageQueueAccountService(IEventSender sender) {
        super(sender);
        instance = this; // needed for service tests!
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return supportedEventTypes;
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
        System.out.println("sending event from MQAccount service");
        Event response = sendRequestAndAwaitReponse(merchantId,getMerchant);
        System.out.println("received event from MQAccount service");
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
        Event response = sendRequestAndAwaitReponse(customerId,getCustomer);
        if(response.isSuccessReponse())
            return response.getPayloadAs(Customer.class);
        else
            throw new CustomerDoesNotExistException(response.getErrorMessage());
    }
}
