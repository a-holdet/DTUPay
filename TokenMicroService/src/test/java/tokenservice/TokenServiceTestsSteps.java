/*
package tokenservice;

import com.google.gson.reflect.TypeToken;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import tokenservice.interfaces.ITokenRepository;
import tokenservice.interfaces.ITokenService;
import tokenservice.tokenservice.LocalTokenService;
import tokenservice.MQ.MQTokenService;
import tokenservice.tokenservice.TokenInMemoryRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenServiceTestsSteps {

    private static class MockEventSender implements IEventSender {

        Event event;
        CompletableFuture<Boolean> eventSet;

        public MockEventSender(CompletableFuture<Boolean> eventSet) {
            this.event = null;
            this.eventSet = eventSet;
        }

        @Override
        public void sendEvent(Event event) {
            this.event = event;
        }

        @Override
        public void sendErrorEvent(EventType eventType, Exception exception, UUID eventID) {}
    }

    MQTokenService s;
    String customerId;
    ITokenRepository tokenRepository = new TokenInMemoryRepository();
    MockEventSender mockEventSender;

    public TokenServiceTestsSteps() {
        ITokenService tokenService = new LocalTokenService(customerId -> true, tokenRepository);
        mockEventSender = new MockEventSender(new CompletableFuture<>());
        s = new MQTokenService(mockEventSender, tokenService);
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
        mockEventSender.eventSet.join();
        assertEquals("createTokensForCustomerSuccess", mockEventSender.event.getEventType());
        List<UUID> tokens = mockEventSender.event.getArgument(0, new TypeToken<>() {});
        assertEquals(amount, tokens.size());
    }
}
*/
