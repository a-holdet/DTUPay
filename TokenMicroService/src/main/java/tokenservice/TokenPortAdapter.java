package tokenservice;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TokenPortAdapter implements IEventReceiver {

    private CompletableFuture<Boolean> customerExistsResult;
    private IEventSender sender;
    private ITokenService tokenService;

    public TokenPortAdapter(ITokenService tokenService, IEventSender sender) {
        this.tokenService = tokenService;
        this.sender = sender;
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("event type in tokenportadapter "+event.getEventType());
        if (event.getEventType().equals("customerExistsResponse")) {
            System.out.println("customerExcists response token adapter");
            customerExistsResponse(event);
        } else if (event.getEventType().equals("consumeToken")) {
            consumeToken(event);
        } else if (event.getEventType().equals("createTokensForCustomer")) {
            createTokensForCustomer(event);
        }
    }

    private void createTokensForCustomer(Event event) {
        System.out.println("reached tokenportadapter");
        System.out.println(event);
        String customerId = (String) event.getArguments()[0];
        System.out.println(customerId);
        String amount = (String) event.getArguments()[1];

        System.out.println(amount);
        System.out.println("reached tokenportadapter");
        // if (!tokenPortAdapter.customerExists(customerId))

        try {
            customerExistsResult = new CompletableFuture<>();
            sender.sendEvent(new Event("customerExists", new Object[]{customerId}));
            boolean customerExists = customerExistsResult.join();
            if (!customerExists) {
                Event customerNotFoundEvent = new Event("createTokensForCustomerFailed",
                        new Object[]{new CustomerNotFoundException("Customer was not found")});
                sender.sendEvent(customerNotFoundEvent);
            } else {
                System.out.println("inside else statement");
                List<UUID> tokens = tokenService.createTokensForCustomer(customerId, Integer.parseInt(amount));
                Event createTokensResponse = new Event("createTokensForCustomerResponse", new Object[]{tokens});
                sender.sendEvent(createTokensResponse);
            }
        } catch (IllegalTokenGrantingException e) {
            e.printStackTrace();
        } catch (CustomerNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void consumeToken(Event event) throws Exception {
        UUID customerToken = UUID.fromString((String) event.getArguments()[0]);

        try {
            System.out.println("tokenservice " + tokenService);
            String customerId = tokenService.consumeToken(customerToken);
            sender.sendEvent(new Event("consumeTokenResponse", new Object[]{customerId}));
            System.out.println("returned tokens for customer " + customerId);
        } catch (TokenDoesNotExistException e) {
            System.out.println("failed " + e.getMessage());
            sender.sendEvent(new Event("consumeTokenResponseFail", new Object[]{e.getMessage()}));
            e.printStackTrace();
        }

    }

    private void customerExistsResponse(Event event) {
        System.out.println(event);
        boolean customerExists = (boolean) event.getArguments()[0];
        System.out.println("customerexists res" + customerExists);
        customerExistsResult.complete(customerExists);
    }

    public boolean customerExists(String customerId) {
        try {
            customerExistsResult = new CompletableFuture<>();
            if (customerId == null) return false;
            sender.sendEvent(new Event("customerExists", new Object[]{customerId}));
            return customerExistsResult.join(); // Blocking
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
