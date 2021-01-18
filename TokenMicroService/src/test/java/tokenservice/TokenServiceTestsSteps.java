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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenServiceTestsSteps {
    EventService s;
    Event event;
    String customerId;

    public TokenServiceTestsSteps() {
        s = new EventService(e -> event = e);
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
        assertEquals("customerExists", event.getEventType());
        assertEquals(customerId, event.getArgument(0,String.class));
    }

    /*
    @Then("a customer  {int} tokens")




    @When("I receive event {string}")
    public void iReceiveEvent(String string) throws Exception {
        s.receiveEvent(new Event(string));
    }

    @Then("I have sent event {string}")
    public void iHaveSentEvent(String string) {
        Assert.assertEquals(string, event.getEventType());
    }

    @Given("a valid cerchant")
    public void aValidMerchant() {


        merchant = new Merchant();
        merchant.accountId = "123123123";
        merchant.cprNumber = "test";
        merchant.firstName = "test";
        merchant.lastName = "test";
        assertNull(merchant.id);
    }

    @When("I receive event registerMerchant with merchant")
    public void iReceiveEventRegisterMerchantWithMerchant() throws Exception {
        s.receiveEvent(new Event("registerMerchant", new Object[] {merchant}));
    }

    @Then("I have sent event registerMerchantSuccess with registered merchantId")
    public void iHaveSentEventRegisterMerchantSuccessWithRegisteredMerchantId() {
        String type = event.getEventType();
        Assert.assertEquals("registerMerchantSuccess", type);

        String merchantId = event.getArgument(0, String.class);

        assertNotNull(merchantId);
    }

    @Given("An invalid Merchant")
    public void anInvalidMerchant() {
        merchant = new Merchant();
    }

    @And("The Merchant is registered")
    public void theMerchantIsRegistered() throws Exception {
        s.registerMerchant(merchant, null);
        merchant.id = event.getArgument(0, String.class);
        event = null; //overwrite sent event
    }

    @And("The Merchant is not registered")
    public void theMerchantIsNotRegistered() {
        merchant.id = UUID.randomUUID().toString();
    }

    @When("I receive event getMerchant event with merchantId")
    public void iReceiveEventGetMerchantEventWithMerchantId() throws Exception {
        s.receiveEvent(new Event("getMerchant", new Object[] {merchant.id}));
    }
    */


}
