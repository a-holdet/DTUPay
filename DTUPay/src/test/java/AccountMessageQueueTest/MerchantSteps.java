
package AccountMessageQueueTest;

import accountservice.MessageQueueAccountService;
import com.google.gson.Gson;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import DTO.Merchant;
import accountservice.MerchantDoesNotExistException;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/***
 * @Author Michael, s153587
 */

public class MerchantSteps {

    Merchant merchant;
    Event event;
    Object expectedOutput;
    Object actualOutput;
    MessageQueueAccountService accountService;
    CompletableFuture<Object> waitForService;
    CompletableFuture<Object> waitForEventInService;

    public MerchantSteps() {
        accountService = new MessageQueueAccountService(new IEventSender() {
            @Override
            public void sendEvent(Event ev)  {
                event = ev;
                if(waitForEventInService != null) {
                    waitForEventInService.complete(true);
                }
            }
        });
    }

    @Given("a merchant")
    public void aMerchant() {
        merchant = new Merchant();
        merchant.accountId = UUID.randomUUID().toString();
    }

    @And("merchant is not registered")
    public void merchantIsNotRegistered() {
        assertNull(merchant.id);
    }

    @When("I want to register merchant")
    public void iWantToRegisterMerchant() {
        expectedOutput = UUID.randomUUID().toString();
        waitForEventInService = new CompletableFuture<>();
        waitForService = CompletableFuture.supplyAsync(() -> accountService.registerMerchant(merchant));
    }

    @Then("I have sent event {string}")
    public void iHaveSentEvent(String eventType) {
        waitForEventInService.join();
        assertEquals(eventType, event.getEventType());
    }

    @When("I receive event {string}")
    public void iReceiveEvent(String eventType) throws Exception {
        Event ev = new Event(eventType, new Object[] {expectedOutput}, event.getUUID());
        accountService.receiveEvent(ev);
    }

    @Then("service is successful")
    public void serviceIsSuccessful() throws Throwable {
        actualOutput = waitForService.join();
        if(actualOutput instanceof Throwable)
            throw (Throwable) actualOutput;

        Gson gson = new Gson();
        assertEquals(gson.toJson(expectedOutput), gson.toJson(actualOutput));
    }

    @Then("merchant is registered")
    public void merchantIsRegistered() throws Exception {
        merchant.id = UUID.randomUUID().toString();
    }

    @When("I want to get merchant")
    public void iWantToGetMerchant() {
        expectedOutput = merchant;
        waitForEventInService = new CompletableFuture<>();
        waitForService = CompletableFuture.supplyAsync(() -> {
            try {
                return accountService.getMerchant(merchant.id);
            } catch (MerchantDoesNotExistException e) {
                return e;
            }
        });
    }

    @Then("service is unsuccessful")
    public void serviceIsUnsuccessful() {
        actualOutput = waitForService.join();
        assertTrue(actualOutput instanceof Throwable);
    }
}
