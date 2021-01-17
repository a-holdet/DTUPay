package messagequeue;

import DTO.Payment;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import paymentservice.IReportService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MessageQueueReportService implements IReportService, IEventReceiver {

    public static MessageQueueReportService getInstance() {
        if (instance == null) {
            try {
                var ies = EventExchange.instance.getSender();
                MessageQueueReportService service = new MessageQueueReportService(ies);
                new EventQueue().registerReceiver(service);
                instance = service;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        return instance;
    }

    public MessageQueueReportService(IEventSender sender) {
        this.sender = sender;
        instance = this;
    }

    private static MessageQueueReportService instance;
    private static final EventType registerTransaction = new EventType("registerTransaction");
    private final IEventSender sender;
    private final ConcurrentHashMap<UUID, CompletableFuture<Event>> requests = new ConcurrentHashMap<>();

    @Override
    public void registerTransaction(Payment payment, String customerId) {
        UUID requestID = UUID.randomUUID();
        Event request = new Event(registerTransaction.getName(), new Object[] {payment, customerId}, requestID);
        requests.put(requestID, new CompletableFuture<>());

        try {
            this.sender.sendEvent(request);
        } catch (Exception e) {
            throw new Error(e);
        }

        requests.get(request.getUUID()).join();
        // Registering a transaction does not return nor throw anything.
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        if (event.getEventType().equals(registerTransaction.getName())) {
            CompletableFuture<Event> cf = requests.get(event.getUUID());
            if (cf != null) cf.complete(event);
        }
    }
}
