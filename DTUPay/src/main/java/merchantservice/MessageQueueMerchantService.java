package merchantservice;

import io.cucumber.java.an.E;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    }

    static final String registerMerchant = "registerMerchant";
    CompletableFuture<Event> registerMerchantCF;
    @Override
    public String registerMerchant(Merchant merchant) throws IllegalArgumentException {
        registerMerchantCF = new CompletableFuture<>();
        // make request
        Event event = new Event(registerMerchant, new Object[] {merchant});
        try {
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }
        // wait for response
        event = registerMerchantCF.join();
        String type = event.getEventType();
        Object[] arguments = event.getArguments();
        if(type.equals(type+"Success")) {
            Merchant m = (Merchant) arguments[0];
            return m.id;
        }
        else //if(type.equals(type+"Fail"))
        {
            String exceptionType = (String) arguments[0];
            String exceptionMsg = (String) arguments[1];

            //if(exceptionType.equals(IllegalArgumentException.class.getSimpleName()))
            throw new IllegalArgumentException(exceptionMsg);
        }
    }

    static final String getMerchant = "getMerchant";
    CompletableFuture<Event> getMerchantCF;
    @Override
    public Merchant getMerchant(String merchantId) {
        getMerchantCF = new CompletableFuture<>();

        Event event = new Event(getMerchant, new Object[] {merchantId});
        try {
            this.sender.sendEvent(event);
        } catch (Exception e) {
            throw new Error(e);
        }

        event = getMerchantCF.join();
        String type = event.getEventType();
        Object[] arguments = event.getArguments();
        if(type.equals(type+"Success")) {
            Merchant m = (Merchant) arguments[0];
            return m;
        }
        else //if(type.equals(type+"Fail"))
        {
            String exceptionType = (String) arguments[0];
            String exceptionMsg = (String) arguments[1];

            //if(exceptionType.equals(IllegalArgumentException.class.getSimpleName()))
            throw new IllegalArgumentException(exceptionMsg);
        }
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("Event received! : " + event.getEventType());

        switch (event.getEventType()) {
            case registerMerchant+"Success":
            case registerMerchant+"Fail":
                registerMerchantCF.complete(event);
                break;
            case getMerchant+"Success":
            case getMerchant+"Fail":
                getMerchantCF.complete(event);
                break;
            default:
                //ignore, do nothing
                break;
        }

        System.out.println("Event handled! : " + event.getEventType());
    }
}
