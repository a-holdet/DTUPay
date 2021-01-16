package paymentservice;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PaymentPortAdapter implements IEventReceiver {

    public static PaymentPortAdapter instance = null; // startUp(); //cannot be used until after startUp();

    public PaymentPortAdapter(IEventSender sender) {
        this.sender = sender;
    }

    public static void startUp() {
        if (instance == null) {
            try {
                var s = EventExchange.instance.getSender();
                PaymentPortAdapter portAdapter = new PaymentPortAdapter(s);
                new EventQueue().registerReceiver(portAdapter);
                instance = portAdapter;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    IEventSender sender;
    private CompletableFuture<String> consumeTokenResult;

    @Override
    public void receiveEvent(Event event) throws Exception {
        if (event.getEventType().equals("consumeTokenResponse")) {
            String customerId = (String) event.getArguments()[0];
            consumeTokenResult.complete(customerId);
        } else if (event.getEventType().equals("consumeTokenResponseFail")) {
            String errorMessage = (String) event.getArguments()[0];
            consumeTokenResult.completeExceptionally(new Exception(errorMessage));
        }
    }

    public String consumeToken(UUID customerToken) throws Exception {
        consumeTokenResult = new CompletableFuture<>();
        sender.sendEvent(new Event("consumeToken", new Object[]{customerToken}));
        return consumeTokenResult.join();
    }
}
