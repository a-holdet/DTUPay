package tokenservice;

import customerservice.Customer;
import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import io.cucumber.java.an.E;
import merchantservice.EventType;
import merchantservice.MessageQueueAccountService;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MessageQueueTokenService implements IEventReceiver, ITokenService {

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

    private static final EventType registerMerchant = new EventType("registerMerchant");
    private static final EventType getMerchant = new EventType("getMerchant");
    private static final EventType registerCustomer = new EventType("registerCustomer");
    private static final EventType customerExists = new EventType("customerExists");
    private static final EventType getCustomer = new EventType("getCustomer");
    private static final EventType[] supportedEventTypes = {registerMerchant, getMerchant, registerCustomer, customerExists, getCustomer};

    public MessageQueueTokenService(IEventSender sender) {
        this.sender = sender;
        instance = this; // needed for service tests!
    }

    IEventSender sender;
    CompletableFuture<Event> createTokensForCustomerResult;

    @Override
    public void receiveEvent(Event event) {
        if (event.getEventType().equals("consumeTokenResponse")) {
//            String customerId = event.getArgument(0, String.class);
            consumeTokenResult.complete(event);
        } else if (event.getEventType().equals("consumeTokenResponseFailed")) {
//            String errorMessage = event.getArgument(0, String.class);
            consumeTokenResult.complete(event);
        } else if (event.getEventType().equals("createTokensForCustomerSuccess")) {
            createTokensResponse(event);
        } else if (event.getEventType().equals("createTokensForCustomerFailed")) {
            createTokensFailed(event);
        }

    }

    private void createTokensFailed(Event event) {
        createTokensForCustomerResult.complete(event);
    }

    @Override
    public List<UUID> readTokensForCustomer(String cpr) {
        // TODO Implement this probably or delete
        return null;
    }

    @Override
    public List<UUID> createTokensForCustomer(String customerId, int amount) throws Exception, IllegalTokenGrantingException {
        if (customerId == null)
            throw new CustomerNotFoundException("Customer must have a customer id to request tokens");

        createTokensForCustomerResult = new CompletableFuture<>();
        Event event = new Event("createTokensForCustomer", new Object[]{customerId, amount});
        sender.sendEvent(event);

        Event createTokensResponseEvent = createTokensForCustomerResult.join();

        if (createTokensResponseEvent.getEventType().endsWith("Failed")) {
            throw new IllegalTokenGrantingException(createTokensResponseEvent.getArgument(0, String.class));
        }

        return createTokensResponseEvent.getArgument(0, new TypeToken<List<UUID>>() {});
    }

    private CompletableFuture<Event> consumeTokenResult;

    @Override
    public String consumeToken(UUID customerToken) throws Exception, ConsumeTokenException {
        consumeTokenResult = new CompletableFuture<>();
        sender.sendEvent(new Event("consumeToken", new Object[]{customerToken}));
        Event consumeTokenEvent = consumeTokenResult.join();

        if (consumeTokenEvent.getEventType().endsWith("Failed")) {
            String errorMessage = consumeTokenEvent.getArgument(0, String.class);
            throw new ConsumeTokenException(errorMessage);
        }else {
            // TODO: Handle this?
        }

        return consumeTokenEvent.getArgument(0, String.class);
    }

    private void createTokensResponse(Event event) {
        List<UUID> tokens = event.getArgument(0, new TypeToken<List<UUID>>() {});

        createTokensForCustomerResult.complete(event);
    }
}
