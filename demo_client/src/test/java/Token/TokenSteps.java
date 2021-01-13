package Token;

import CustomerMobileApp.TokenGenerationAdapter;
import CustomerMobileApp.UserManagementAdapter;
import DTUPay.TokenHolder;
import DTUPay.UserHolder;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.quarkus.security.UnauthorizedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class TokenSteps {


    //String customerAccountId;
    //String customerId;

    //Exceptions
    UnauthorizedException unauthorizedException;
    //Adapters
    BankService bankService;
    TokenGenerationAdapter tokenAdapter;
    UserManagementAdapter userManagementAdapter;
    //Holders
    TokenHolder tokenHolder = TokenHolder.instance;
    UserHolder customerHolder = UserHolder.customer;

    @Before
    public void setup() {
        this.bankService = new BankServiceService().getBankServicePort();
        this.tokenAdapter = new TokenGenerationAdapter();
        this.userManagementAdapter = new UserManagementAdapter();
    }

    @After
    public void teardown() {
        if (customerHolder.id != null) {
            System.out.println("teardown method");
            tokenAdapter.deleteTokensFor(customerHolder.id);
            tokenHolder.reset();
        }
        try {
            if (customerHolder.accountId != null) bankService.retireAccount(customerHolder.accountId);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            fail();
        }
        customerHolder.accountId = null;
        customerHolder.id = null;
    }

    @And("the customer has {int} tokens")
    public void theCustomerHasTokens(int tokenAmount) {
        assertNotNull(customerHolder.id);
        List<UUID> tokens = tokenAdapter.readTokensForCustomer(customerHolder.id);
        assertEquals(tokenAmount, tokens.size());
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int tokenAmount) {
        try {
            System.out.println("Customer requests tokens");
            System.out.println("Customer id: " + customerHolder.id);
            tokenHolder.setTokens(tokenAdapter.createTokensForCustomer(customerHolder.id, tokenAmount));
        } catch (UnauthorizedException e) {
            this.unauthorizedException = e;
        }
    }

    @Then("the customer gets {int} tokens")
    public void theCustomerGetsTokens(int tokenAmount) {
        assertEquals(tokenAmount, tokenHolder.getTokens().size());
    }

    @Then("the token granting is not successful")
    public void theTokenGrantingIsNotSuccessful() {
        assertNotNull(unauthorizedException);
    }

    @Then("the token granting is denied")
    public void theTokenGrantingIsDenied() {

    }

    @Given("the customer with name {string} {string} and CPR {string} has no bank account")
    public void theCustomerWithNameAndCPRHasNoBankAccount(String firstName, String lastName, String cprNumber) {
        User customer =  new User();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCprNumber(cprNumber);
        this.customerHolder.accountId = null;
    }

    @And("the received error message is {string}")
    public void theReceivedErrorMessageIs(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, unauthorizedException.getMessage());
    }
}
