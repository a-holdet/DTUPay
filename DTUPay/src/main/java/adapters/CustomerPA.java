package adapters;

import customerservice.Customer;
import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import io.cucumber.java.an.E;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CustomerPA implements IEventReceiver {

    IEventSender sender;
    CompletableFuture<List<UUID>> createTokensForCustomerResult;
    CompletableFuture<String> registerCustomerResult;

    public CustomerPA(IEventSender sender) {
        this.sender = sender;
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        if (event.getEventType().equals("createTokensForCustomerResponse") ) {
            createTokensResponse(event);
        } else if (event.getEventType().equals("createTokensForCustomerFailed")) {
            System.out.println("received");
        } else if (event.getEventType().equals("registerCustomerResponse")){
            System.out.println("customerPA " + event);
            registerCustomerResponse(event);
        } else if (event.getEventType().equals("registerCustomerFailed")) {
            System.out.println("customerPA Failed" + event);
        } else {
            System.out.println("CustomerPA ignored: " + event);
        }
    }

    private void registerCustomerResponse(Event event) {
        String customerId = event.getArgument(0, String.class);
        registerCustomerResult.complete(customerId);
    }

    private void createTokensResponse(Event event) {
        System.out.println("inside createTokenresponse");
        List<UUID> tokens = (List<UUID>) event.getArguments()[0];
        createTokensForCustomerResult.complete(tokens);
    }

    public List<UUID> createTokensForCustomer(String customerId, int amount) throws Exception {
        System.out.println("inside createTokensForCustomer");
        createTokensForCustomerResult = new CompletableFuture<>();
        Event event = new Event("createTokensForCustomer", new Object[]{customerId, amount});
        System.out.println("evente cpa " + event);
        sender.sendEvent(event);
        return createTokensForCustomerResult.join();
    }

    public String registerCustomer(Customer customer) throws Exception {
//        TODO: Call customer micro service to register customer
        System.out.println("Inside register customer method in CustomerPA");
        registerCustomerResult = new CompletableFuture<>();
        Event event = new Event("registerCustomer", new Object[]{customer});
        sender.sendEvent(event);
        return registerCustomerResult.join();
    }
}
