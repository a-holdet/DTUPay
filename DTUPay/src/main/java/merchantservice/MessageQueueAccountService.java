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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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

    private final IEventSender sender;
    public MessageQueueAccountService(IEventSender sender) {
        this.sender = sender;
        instance = this; // needed for service tests!
    }

    private final ConcurrentHashMap<UUID, CompletableFuture<Event>> responses = new ConcurrentHashMap<>();

/*    private <S, F> S handle(Object payload, EventType eventType) {
        ...
    }*/

    private Event sendPayloadAndWait(Object payload, EventType eventType) {
        Event event = new Event(eventType.getName(), new Object[] {payload}, UUID.randomUUID());
        responses.put(event.getUUID(), new CompletableFuture<>());

        try {
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        return responses.get(event.getUUID()).join();
    }

    @Override
    public String registerMerchant(Merchant merchant) throws IllegalArgumentException {
        Event event = sendPayloadAndWait(merchant, registerMerchant);
        String type = event.getEventType();

        if(type.equals(registerMerchant.succeeded())) {
            return event.getArgument(0, String.class); // merchantId
        }

        String exceptionType = event.getArgument(0, String.class);
        String exceptionMsg  = event.getArgument(1, String.class);
        throw new IllegalArgumentException(exceptionMsg);
    }


    @Override
    public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
        Event event = sendPayloadAndWait(merchantId, getMerchant);
        String type = event.getEventType();

        if(type.equals(getMerchant.succeeded())) {
            return event.getArgument(0, Merchant.class); // merchant
        }

        String exceptionType = event.getArgument(0, String.class);
        String exceptionMsg  = event.getArgument(1, String.class);
        throw new MerchantDoesNotExistException(exceptionMsg);
    }

    @Override
    public String registerCustomer(Customer customer) throws IllegalArgumentException {
        Event event = sendPayloadAndWait(customer, registerCustomer);
        String type = event.getEventType();

        if(type.equals(registerCustomer.succeeded())) {
            return event.getArgument(0, String.class); // customerId
        }

        System.out.println("XXXX");
        System.out.println(type);
        System.out.println(registerCustomer.succeeded());
        System.out.println(registerCustomer.failed());

        String exceptionType = event.getArgument(0, String.class);
        String exceptionMsg  = event.getArgument(1, String.class);
        throw new IllegalArgumentException(exceptionMsg);
    }

    @Override
    public boolean customerExists(String customerId)  {
        Event event = sendPayloadAndWait(customerId, customerExists);
        String type = event.getEventType();

        if (type.equals(customerExists.succeeded())) {
            return event.getArgument(0, Boolean.class); // wether customer exists or not
        } else {
            return false; // some error happened. Interpreted as customer does not exist.
        }
    }

    @Override
    public Customer getCustomer(String customerId) throws CustomerDoesNotExistException {
        Event event = sendPayloadAndWait(customerId, getCustomer);
        String type = event.getEventType();

        if(type.equals(getCustomer.succeeded())) {
            return event.getArgument(0, Customer.class); // customer
        }

        String exceptionType = event.getArgument(0, String.class);
        String exceptionMsg  = event.getArgument(1, String.class);
        throw new CustomerDoesNotExistException(exceptionMsg);
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        if (Arrays.stream(supportedEventTypes).anyMatch(eventType -> eventType.matches(event.getEventType()))) {
            CompletableFuture<Event> cf = responses.get(event.getUUID());
            if (cf != null) cf.complete(event);
        }

        System.out.println("--------------------------------------------------------");
    }
}
