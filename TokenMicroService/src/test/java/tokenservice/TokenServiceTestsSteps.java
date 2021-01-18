package tokenservice;

import com.google.gson.reflect.TypeToken;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.rmq.event.objects.Event;
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
    MQTokenService s;
    Event event;
    String customerId;
    ITokenRepository tokenRepository = new TokenInMemoryRepository();
    CompletableFuture<Boolean> eventSet = new CompletableFuture<>();

    public TokenServiceTestsSteps() {
        ITokenService tokenService = new LocalTokenService(customerId -> true, tokenRepository);

        s = new MQTokenService(e -> {
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
