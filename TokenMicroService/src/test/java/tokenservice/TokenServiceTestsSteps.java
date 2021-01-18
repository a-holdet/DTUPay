package tokenservice;

import com.google.gson.reflect.TypeToken;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.rmq.event.objects.Event;
import tokenservice.customer.ICustomerService;
import tokenservice.messagequeue.EventService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenServiceTestsSteps {
    EventService s;
    Event event;
    String customerId;
    CompletableFuture<Boolean> eventSet = new CompletableFuture<>();

    public TokenServiceTestsSteps() {
        ITokenService tokenService = new LocalTokenService(customerId -> true);

        s = new EventService(e -> {
            event = e;
            eventSet.complete(true);
        }, tokenService);
    }

    @Given("a valid customer")
    public void aValidCustomer() {
        customerId = "123456";
    }

    @When("I receive event createTokensForCustomer with {int} tokens")
    public void iReceiveEventWithTokens(int amount) {
        s.receiveEvent(new Event("createTokensForCustomer", new Object[]{customerId, amount}));
    }

    @Then("I have sent event createTokensForCustomerSuccess with {int} tokens")
    public void iHaveSentEventWithTokens(int amount) {
        eventSet.join();
        assertEquals("createTokensForCustomerSuccess",event.getEventType());
        List<UUID> tokens = event.getArgument(0, new TypeToken<>() {});
        assertEquals(amount, tokens.size());
    }
}
