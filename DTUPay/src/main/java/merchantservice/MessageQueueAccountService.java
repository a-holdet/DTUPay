package merchantservice;

import customerservice.Customer;
import customerservice.CustomerDoesNotExistException;
import customerservice.ICustomerService;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class MessageQueueAccountService implements IMerchantService, ICustomerService, IEventReceiver {

    // Singleton as method due to serviceTest
    private static MessageQueueAccountService instance;
    public static MessageQueueAccountService getInstance() {
        if (instance == null) {
            try {
                var ies = EventExchange.instance.getSender();
                MessageQueueAccountService service = new MessageQueueAccountService(ies);
                new EventQueue().registerReceiver(service);
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
    private static final EventType[] supportedEventTypes = {registerMerchant, getMerchant, registerCustomer, customerExists, getCustomer};

    private final ConcurrentHashMap<UUID, CompletableFuture<Event>> requests = new ConcurrentHashMap<>();
    private final IEventSender sender;

    public MessageQueueAccountService(IEventSender sender) {
        this.sender = sender;
        instance = this; // needed for service tests!
    }

    private Event sendRequestAndAwaitReponse(Object payload, EventType eventType){
        Event request = new Event(eventType.getName(), new Object[] {payload}, UUID.randomUUID());
        requests.put(request.getUUID(), new CompletableFuture<>());

        try {
            this.sender.sendEvent(request);
        } catch (Exception e) {
            throw new Error(e);
        }

        Event response = requests.get(request.getUUID()).join();
        return response;
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
        Event response = sendRequestAndAwaitReponse(customerId,getCustomer);
        if(response.isSuccessReponse())
            return response.getPayloadAs(Customer.class);
        else
            throw new CustomerDoesNotExistException(response.getErrorMessage());
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        if (Arrays.stream(supportedEventTypes).anyMatch(eventType -> eventType.matches(event.getEventType()))) {
            CompletableFuture<Event> cf = requests.get(event.getUUID());
            if (cf != null)
                cf.complete(event);
        }

        System.out.println("--------------------------------------------------------");
    }
}
