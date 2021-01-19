package reportservice;

import messaging.rmq.event.objects.MessageQueueBase;
import paymentservice.Payment;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MessageQueueReportService extends MessageQueueBase implements IReportService {

    private final EventType registerTransaction = new EventType("registerTransaction");

    public MessageQueueReportService(IEventSender sender) {
        super(sender);
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return new EventType[]{registerTransaction};
    }

    @Override
    public void registerTransaction(Payment payment, String customerId) {
        // Registering a transaction does not return nor throw anything.
        // Thus, we can safely drop the returned event here
        sendRequest(payment, registerTransaction);
    }
}
