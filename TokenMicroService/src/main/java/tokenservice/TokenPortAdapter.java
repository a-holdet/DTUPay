package tokenservice;

import io.cucumber.java.an.E;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.concurrent.CompletableFuture;

public class TokenPortAdapter implements IEventReceiver {

    public static TokenPortAdapter instance = null; // startUp(); //cannot be used until after startUp();

    public TokenPortAdapter(IEventSender sender) {
        this.sender = sender;
    }


    public static void startUp() {
        if (instance == null) {
            try {
                var s = EventExchange.instance.getSender();
                TokenPortAdapter portAdapter = new TokenPortAdapter(s);
                new EventQueue().registerReceiver(portAdapter);
                instance = portAdapter;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    IEventSender sender;
    private CompletableFuture<Boolean> customerExistsResult;

    @Override
    public void receiveEvent(Event event) throws Exception {
        if (event.getEventType().equals("customerExists")){
            sender.sendEvent(new Event("customerExistsResponse"));
        } else if (event.getEventType().equals("customerExistsResponse")) {
            customerExistsResult.complete(true);
            System.out.println("yay");
        }
    }


    public boolean customerExists(String customerId) {
        try {
            customerExistsResult = new CompletableFuture<>();
            sender.sendEvent(new Event("customerExists"));
            return customerExistsResult.join(); // Blocking
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
