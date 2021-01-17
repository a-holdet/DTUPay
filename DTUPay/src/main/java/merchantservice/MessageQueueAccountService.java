package merchantservice;

import customerservice.Customer;
import customerservice.CustomerDoesNotExistException;
import customerservice.ICustomerService;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

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

    IEventSender sender;
    public MessageQueueAccountService(IEventSender sender) {
        this.sender = sender;
        instance = this; // needed for service tests!
    }

    static final String registerMerchant = "registerMerchant";
    ConcurrentHashMap<UUID, CompletableFuture<Event>> registerMerchantCF = new ConcurrentHashMap<>();
    @Override
    public String registerMerchant(Merchant merchant) throws IllegalArgumentException {
        Event event = new Event(registerMerchant, new Object[] {merchant}, UUID.randomUUID());
        try {
            registerMerchantCF.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        System.out.println("A");
        event = registerMerchantCF.get(event.getUUID()).join();
        String type = event.getEventType();
        System.out.println("A");

        if(type.equals(registerMerchant+"Success")) {
            String merchantId = event.getArgument(0, String.class);
            return merchantId;
        }

        String exceptionType = event.getArgument(0, String.class);
        String exceptionMsg  = event.getArgument(1, String.class);
        throw new IllegalArgumentException(exceptionMsg);
    }

    static final String getMerchant = "getMerchant";
    ConcurrentHashMap<UUID, CompletableFuture<Event>> getMerchantCF = new ConcurrentHashMap<>();
    @Override
    public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
        Event event = new Event(getMerchant, new Object[] {merchantId}, UUID.randomUUID());
        try {
            getMerchantCF.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        System.out.println("B");
        event = getMerchantCF.get(event.getUUID()).join();
        String type = event.getEventType();
        System.out.println("B");

        if(type.equals(getMerchant+"Success")) {
            Merchant merchant = event.getArgument(0, Merchant.class);
            return merchant;
        }

        String exceptionType = event.getArgument(0, String.class);
        String exceptionMsg  = event.getArgument(1, String.class);
        throw new MerchantDoesNotExistException(exceptionMsg);
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        CompletableFuture<Event> cf;
        switch (event.getEventType()) {
            case registerMerchant+"Success":
            case registerMerchant+"Fail":
                cf = registerMerchantCF.getOrDefault(event.getUUID(), null);
                if(cf != null)
                    cf.complete(event);
                break;
            case getMerchant+"Success":
            case getMerchant+"Fail":
                cf = getMerchantCF.getOrDefault(event.getUUID(), null);
                if(cf != null)
                    cf.complete(event);
                break;
            case getCustomer+"Success":
            case getCustomer+"Fail":
                cf = getCustomerCF.getOrDefault(event.getUUID(), null);
                if(cf != null)
                    cf.complete(event);
                break;
            case registerCustomer+"Success":
            case registerCustomer+"Fail":
                cf = registerCustomerCF.getOrDefault(event.getUUID(), null);
                if(cf != null)
                    cf.complete(event);
                break;
            case customerExists+"Success":
            case customerExists+"Fail":
                cf = customerExistsCF.getOrDefault(event.getUUID(), null);
                if(cf != null)
                    cf.complete(event);
                break;
            default:
                //ignore, do nothing
                System.out.println("AAAA " + event.getEventType());
                break;
        }

        System.out.println("--------------------------------------------------------");
    }

    private static final String registerCustomer = "registerCustomer";
    ConcurrentHashMap<UUID, CompletableFuture<Event>> registerCustomerCF = new ConcurrentHashMap<>();
    @Override
    public String registerCustomer(Customer customer) throws IllegalArgumentException {
        Event event = new Event(registerCustomer, new Object[] {customer}, UUID.randomUUID());
        try {
            registerCustomerCF.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        System.out.println("C");
        event = registerCustomerCF.get(event.getUUID()).join();
        String type = event.getEventType();
        System.out.println("C");

        if(type.equals(registerCustomer+"Success")) {
            String customerId = event.getArgument(0, String.class);
            return customerId;
        }

        String exceptionType = event.getArgument(0, String.class);
        String exceptionMsg  = event.getArgument(1, String.class);
        throw new IllegalArgumentException(exceptionMsg);
    }

    private static final String customerExists = "customerExists";
    ConcurrentHashMap<UUID, CompletableFuture<Event>> customerExistsCF = new ConcurrentHashMap<>();
    @Override
    public boolean customerExists(String customerId)  {
        Event event = new Event(customerExists, new Object[] {customerId}, UUID.randomUUID());
        try {
            customerExistsCF.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        System.out.println("D");
        event = customerExistsCF.get(event.getUUID()).join();
        String type = event.getEventType();
        System.out.println("D");

        if (type.equals(customerExists+"Success")) {
            boolean exists = event.getArgument(0, Boolean.class);
            return exists;
        } else {
            return false;
        }
    }

    private static final String getCustomer = "getCustomer";
    ConcurrentHashMap<UUID, CompletableFuture<Event>> getCustomerCF = new ConcurrentHashMap<>();
    @Override
    public Customer getCustomer(String customerId) throws CustomerDoesNotExistException {
        Event event = new Event(getCustomer, new Object[] {customerId}, UUID.randomUUID());
        try {
            getCustomerCF.put(event.getUUID(), new CompletableFuture<>());
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        System.out.println("E");
        event = getCustomerCF.get(event.getUUID()).join();
        String type = event.getEventType();
        System.out.println("E");

        if(type.equals(getCustomer+"Success")) {
            Customer customer = event.getArgument(0, Customer.class);
            return customer;
        }

        String exceptionType = event.getArgument(0, String.class);
        String exceptionMsg  = event.getArgument(1, String.class);
        throw new CustomerDoesNotExistException(exceptionMsg);
    }
}
