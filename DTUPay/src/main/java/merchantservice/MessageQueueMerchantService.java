package merchantservice;

import io.cucumber.java.an.E;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MessageQueueMerchantService implements IMerchantService, IEventReceiver {

    // Singleton as method due to serviceTest
    private static MessageQueueMerchantService instance;
    public static MessageQueueMerchantService getInstance() {
        if (instance == null) {
            try {
                var ies = EventExchange.instance.getSender();
                MessageQueueMerchantService service = new MessageQueueMerchantService(ies);
                new EventQueue().registerReceiver(service);
                instance = service;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        return instance;
    }

    IEventSender sender;
    public MessageQueueMerchantService(IEventSender sender) {
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

        event = registerMerchantCF.get(event.getUUID()).join();
        String type = event.getEventType();

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

        event = getMerchantCF.get(event.getUUID()).join();
        String type = event.getEventType();

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
            default:
                //ignore, do nothing
                break;
        }

        System.out.println("--------------------------------------------------------");
    }
}
