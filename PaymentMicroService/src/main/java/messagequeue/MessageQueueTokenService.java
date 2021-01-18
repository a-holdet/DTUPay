package messagequeue;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.Result;
import Tokens.ITokenService;
import Tokens.TokenDoesNotExistException;

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
                new EventQueue(service).startListening();
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
        System.out.println("Making request. ID = " + requestID);
        Event request = new Event(eventType.getName(), new Object[] {payload}, requestID);
        requests.put(requestID, new CompletableFuture<>());

        try {
            this.sender.sendEvent(request);
        } catch (Exception e) {
            throw new Error(e);
        }

        Event response = requests.get(request.getUUID()).join();
        String type = response.getEventType();

        System.out.println("MESSAGE QUEUE TOKEN SERVICE!");
        System.out.println(type);
        System.out.println(eventType.succeeded());
        System.out.println(eventType.failed());
        System.out.println(response.getArgument(0, String.class) == null);
        System.out.println(response.getArgument(0, String.class));


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
    public void receiveEvent(Event event) {
        System.out.println("--------------------------------------------------------");
        System.out.println("Event received! : " + event);

        if (consumeToken.matches(event.getEventType())) {
            System.out.println("Receive Event mathes:" + event.getEventType());
            CompletableFuture<Event> cf = requests.get(event.getUUID());
            if (cf != null) {
                System.out.println("Completing future");
                cf.complete(event);
            } else {
                System.out.println("Future is null. Event id: " + event.getUUID());
            }
        } else {
            System.out.println("Ignores " + event.getEventType());
        }
    }

    @Override
    public String consumeToken(UUID customerToken) throws TokenDoesNotExistException {
        System.out.println("MESSAGE QUEUE TOKEN SERVICE: consume token: " + customerToken);
        Result<String, String> res = handle(customerToken, consumeToken, String.class);
        System.out.println("MESSAGE QUEUE TOKEN SERVICE: consume token received: " + res);
        if (res.state == Result.ResultState.FAILURE) {
            throw new TokenDoesNotExistException(res.failureValue);
        } else {
            return res.successValue;
        }
    }
}
