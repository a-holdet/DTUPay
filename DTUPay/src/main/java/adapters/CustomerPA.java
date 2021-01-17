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

    public CustomerPA(IEventSender sender) {
        this.sender = sender;
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        if (event.getEventType().equals("createTokensForCustomerResponse") ) {
            createTokensResponse(event);
        } else if (event.getEventType().equals("createTokensForCustomerFailed")) {
            System.out.println("received");
        } else {
            System.out.println("customerPA " + event);
        }
    }

    private void createTokensResponse(Event event) {
        System.out.println("inside createTokenresponse");
        List<UUID> tokens = (List<UUID>) event.getArguments()[0];
        createTokensForCustomerResult.complete(tokens);
    }

    public List<UUID> createTokensForCustomer(String customerId, int amount) throws Exception {
        System.out.println("inside createTokensForCustomer");
        createTokensForCustomerResult = new CompletableFuture<>();
        Event event = new Event("createTokensForCustomer", new Object[]{customerId, String.valueOf(amount)});
        sender.sendEvent(event);
        System.out.println("didSEndEvent in createTokenForCustomers");
        return createTokensForCustomerResult.join();
    }

    public String registerCustomer(Customer customer) {
//        TODO: Call customer micro service to register customer
        return null;
    }
}
