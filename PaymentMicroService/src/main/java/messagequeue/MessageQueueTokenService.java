package messagequeue;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.Result;
import paymentservice.ITokenService;
import paymentservice.TokenDoesNotExistException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MessageQueueTokenService implements ITokenService, IEventReceiver {

    // Singleton as method due to serviceTest
    private static MessageQueueTokenService instance;
    public static MessageQueueTokenService getInstance() {
        if (instance == null) {
            try {
                var ies = EventExchange.instance.getSender();
                MessageQueueTokenService service = new MessageQueueTokenService(ies);
                new EventQueue().registerReceiver(service);
                instance = service;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        return instance;
    }

    private static final EventType consumeToken = new EventType("consumeToken");
    private final IEventSender sender;
    private final ConcurrentHashMap<UUID, CompletableFuture<Event>> requests = new ConcurrentHashMap<>();

    public MessageQueueTokenService(IEventSender sender) {
        this.sender = sender;
        instance = this;
    }

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
    public void receiveEvent(Event event) throws Exception {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        if (consumeToken.getName().equals(event.getEventType())) {
            CompletableFuture<Event> cf = requests.get(event.getUUID());
            if (cf != null) cf.complete(event);
        }

        System.out.println("--------------------------------------------------------");
    }

    @Override
    public String consumeToken(UUID customerToken) throws TokenDoesNotExistException {
        Result<String, String> res = handle(customerToken, consumeToken, String.class);

        if (res.state == Result.ResultState.FAILURE) {
            throw new TokenDoesNotExistException(res.failureValue);
        } else {
            return res.successValue;
        }
    }
}
