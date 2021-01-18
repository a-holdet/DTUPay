package tokenservice;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import org.junit.Assert;
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
        s = new EventService(e -> {
            event = e;
            eventSet.complete(true);
        });
    }

    @Given("a valid customer")
    public void aValidCustomer() {
        customerId = "123456";
    }

    @When("I receive event createTokensForCustomer with {int} tokens")
    public void iReceiveEventWithTokens(int amount) throws Exception {
        s.receiveEvent(new Event("createTokensForCustomer", new Object[]{customerId, amount}));
    }

    @Then("I have sent event createTokensForCustomerSuccess with {int} tokens")
    public void iHaveSentEventWithTokens(int amount) {
        assertEquals("createTokensForCustomerSuccess",event.getEventType());
        List<UUID> tokens = (List<UUID>) event.getArguments()[0];
        assertEquals(amount, tokens.size());
    }

    @When("I receive event customerExistsSuccess")
    public void iReceiveEventCustomerExistsSuccess() throws Exception {
        s.receiveEvent(new Event("customerExistsSuccess", new Object[]{true}));
    }

    @Then("I sent customerExists")
    public void iSentCustomerExists() {
        eventSet.join();
        assertEquals("customerExists", event.getEventType());
        assertEquals(customerId, event.getArgument(0,String.class));
    }
}
