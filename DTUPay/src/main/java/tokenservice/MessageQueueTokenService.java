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
        } else if (event.getEventType().equals("createTokensForCustomerResponse")) {
            createTokensResponse(event);
        } else if (event.getEventType().equals("createTokensForCustomerFailed")) {
            createTokensFailed(event);
        } else {
            System.out.println("CustomerPA ignored: " + event);
        }

//        else if (event.getEventType().equals("registerCustomerResponse")) {
//            System.out.println("customerPA " + event);
//            registerCustomerResponse(event);
//        } else if (event.getEventType().equals("registerCustomerFailed")) {
//            System.out.println("customerPA Failed" + event);
//        }
    }

    private void createTokensFailed(Event event) {
        createTokensForCustomerResult.complete(event);
    }
//
//    private void registerCustomerResponse(Event event) {
//        String customerId = event.getArgument(0, String.class);
//        registerCustomerResult.complete(customerId);
//    }



    @Override
    public List<UUID> readTokensForCustomer(String cpr) {

        return null;
    }

    @Override
    public List<UUID> createTokensForCustomer(String customerId, int amount) throws Exception, IllegalTokenGrantingException {
        System.out.println("inside createTokensForCustomer");
        // if customer id is null, then never call createTokenForCustomer
        if (customerId == null)
            throw new CustomerNotFoundException("Customer must have a customer id to request tokens");

        createTokensForCustomerResult = new CompletableFuture<>();
        System.out.println("cust id " + customerId);
        Event event = new Event("createTokensForCustomer", new Object[]{customerId, amount});
        System.out.println("evente cpa " + event);
        sender.sendEvent(event);

        Event createTokensResponseEvent = createTokensForCustomerResult.join();

        if (createTokensResponseEvent.getEventType().endsWith("Failed")) {
            throw new IllegalTokenGrantingException(createTokensResponseEvent.getArgument(0, String.class));
        }

        return (List<UUID>) createTokensResponseEvent.getArguments()[0];
    }
//
//    public String registerCustomer(Customer customer) throws Exception {
////        TODO: Call customer micro service to register customer
//        System.out.println("Inside register customer method in CustomerPA");
//        registerCustomerResult = new CompletableFuture<>();
//        Event event = new Event("registerCustomer", new Object[]{customer});
//        sender.sendEvent(event);
//        return registerCustomerResult.join();
//    }

    private CompletableFuture<Event> consumeTokenResult;

    @Override
    public String consumeToken(UUID customerToken) throws Exception, ConsumeTokenException {
        consumeTokenResult = new CompletableFuture<>();
        sender.sendEvent(new Event("consumeToken", new Object[]{customerToken}));
        Event consumeTokenEvent = consumeTokenResult.join();

        if (consumeTokenEvent.getEventType().endsWith("Failed")) {
            System.out.println("in heere");
            String errorMessage = consumeTokenEvent.getArgument(0, String.class);
            throw new ConsumeTokenException(errorMessage);
        }else {
            System.out.println("not in here " + consumeTokenEvent);
        }

        return consumeTokenEvent.getArgument(0, String.class);
    }

    private void createTokensResponse(Event event) {
        System.out.println("inside createTokenresponse");
        List<UUID> tokens = (List<UUID>) event.getArguments()[0];

        createTokensForCustomerResult.complete(event);
    }
}
