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

    private final IEventSender sender;
    public MessageQueueAccountService(IEventSender sender) {
        this.sender = sender;
        instance = this; // needed for service tests!
    }

    private final ConcurrentHashMap<UUID, CompletableFuture<Event>> requests = new ConcurrentHashMap<>();

    private <S> Result<S, String> handle(Object payload, EventType eventType, Class<S> successClass) throws Error {
        UUID requestID = UUID.randomUUID();
        Event request = new Event(eventType.getName(), new Object[] {payload}, requestID);
        requests.put(requestID, new CompletableFuture<>());

        try {
            this.sender.sendEvent(request);
        } catch (Exception e) {
            throw new Error(e);
        }

        Event response = requests.get(request.getUUID()).join();
        String type = response.getEventType();

        if (type.equals(eventType.succeeded())) {
            S success = response.getArgument(0, successClass);
            return new Result<>(success, null, Result.ResultState.SUCCESS);
        } else {
            String exceptionType = response.getArgument(0, String.class); // TODO: remove?
            String failure = response.getArgument(1, String.class);
            return new Result<>(null, failure, Result.ResultState.FAILURE);
        }
    }

    @Override
    public String registerMerchant(Merchant merchant) throws IllegalArgumentException {
        Result<String, String> res = handle(merchant, registerMerchant, String.class);
        if (res.state == Result.ResultState.FAILURE) {
            throw new IllegalArgumentException(res.failureValue);
        } else {
            return res.successValue;
        }
    }

    @Override
    public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
        Result<Merchant, String> res = handle(merchantId, getMerchant, Merchant.class);
        if (res.state == Result.ResultState.FAILURE) {
            throw new MerchantDoesNotExistException(res.failureValue);
        } else {
            return res.successValue;
        }
    }

    @Override
    public String registerCustomer(Customer customer) throws IllegalArgumentException {
        Result<String, String> res = handle(customer, registerCustomer, String.class);
        if (res.state == Result.ResultState.FAILURE) {
            throw new IllegalArgumentException(res.failureValue);
        } else {
            return res.successValue;
        }
    }

    @Override
    public boolean customerExists(String customerId)  {
        Result<Boolean, String> res = handle(customerId, customerExists, Boolean.class);
        if (res.state == Result.ResultState.FAILURE) {
            return false;
        } else {
            return res.successValue;
        }
    }

    @Override
    public Customer getCustomer(String customerId) throws CustomerDoesNotExistException {
        Result<Customer, String> res = handle(customerId, getCustomer, Customer.class);

        if (res.state == Result.ResultState.FAILURE) {
            throw new CustomerDoesNotExistException(res.failureValue);
        } else {
            return res.successValue;
        }
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        if (Arrays.stream(supportedEventTypes).anyMatch(eventType -> eventType.matches(event.getEventType()))) {
            CompletableFuture<Event> cf = requests.get(event.getUUID());
            if (cf != null) cf.complete(event);
        }

        System.out.println("--------------------------------------------------------");
    }
}
