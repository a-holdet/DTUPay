package messagequeue;


import DTO.Customer;
import DTO.Merchant;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import messaging.rmq.event.objects.Result;
import Accounts.CustomerDoesNotExistException;
import Accounts.ICustomerService;
import Accounts.IMerchantService;
import Accounts.MerchantDoesNotExistException;

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
                var ies = new EventExchangeFactory().getExchange().createIEventSender();
                MessageQueueAccountService service = new MessageQueueAccountService(ies);
                instance = service;
                //new EventQueue(instance).startListening();
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        return instance;
    }

    private static final EventType getMerchant = new EventType("getMerchant");
    private static final EventType getCustomer = new EventType("getCustomer");
    private static final EventType[] supportedEventTypes = {getMerchant, getCustomer};

    private final IEventSender sender;
    public MessageQueueAccountService(IEventSender sender) {
        this.sender = sender;
        instance = this; // needed for service tests!
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return supportedEventTypes;
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
    public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
        System.out.println("GET MERCHANT SEND REQUEST");
        Result<Merchant, String> res = handle(merchantId, getMerchant, Merchant.class);
        System.out.println("GET MERCHANT RECEIVE REQUEST");
        if (res.state == Result.ResultState.FAILURE) {
            throw new MerchantDoesNotExistException(res.failureValue);
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
    public void receiveEvent(Event event) {
        System.out.println("--------------------------------------------------------");

        if (Arrays.stream(supportedEventTypes).anyMatch(eventType -> eventType.matches(event.getEventType()))) {
            System.out.println("Supported Event received! : " + event);
            CompletableFuture<Event> cf = requests.get(event.getUUID());
            if (cf != null) cf.complete(event);
        } else {
            System.out.println("Event received! : " + event);
        }

        System.out.println("--------------------------------------------------------");
    }
}
