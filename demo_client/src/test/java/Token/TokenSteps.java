package Token;

import CustomerMobileApp.CustomerCannotRequestMoreTokensException;
import CustomerMobileApp.TokenGenerationAdapter;
import CustomerMobileApp.UserManagementAdapter;
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

    User customer;
    String customerAccountId;
    String customerId;
    List<UUID> tokens;
    BankService bankService;
    Exception exception;
    TokenGenerationAdapter tokenAdapter;
    UserManagementAdapter userManagementAdapter;

    @Before
    public void setup() {
        this.bankService = new BankServiceService().getBankServicePort();
        this.tokenAdapter = new TokenGenerationAdapter();
        this.userManagementAdapter = new UserManagementAdapter();
        this.tokens = new ArrayList<>();
    }

    @After
    public void teardown() {
        if (customer != null) tokenAdapter.deleteTokensFor(customerId);
        try {
            if (customerAccountId != null) bankService.retireAccount(customerAccountId);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            fail();
        }
        customerAccountId = null;
        customerId = null;
        exception = null;
    }

    @Given("the customer with name {string} {string} and CPR {string} has a bank account")
    public void theCustomerWithNameAndCPRHasABankAccount(String firstName, String lastName, String cpr) {
        this.customer = new User();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCprNumber(cpr);
        try {
            this.customerAccountId = bankService.createAccountWithBalance(customer, BigDecimal.ZERO);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @And("the customer has {int} tokens")
    public void theCustomerHasTokens(int tokenAmount) {
        assertNotNull(customerId);
        List<UUID> tokens = tokenAdapter.readTokensForCustomer(customerId);
        assertEquals(tokenAmount, tokens.size());
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int tokenAmount) {
        try {
            tokens = tokenAdapter.createTokensForCustomer(customerId, tokenAmount);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("the customer gets {int} tokens")
    public void theCustomerGetsTokens(int tokenAmount) {
        assertEquals(tokenAmount, tokens.size());
    }

    @Then("the token granting is not successful")
    public void theTokenGrantingIsNotSuccessful() {
        assertNotNull(exception);
    }

    @And("the customer is registered at DTUPay")
    public void theCustomerIsRegisteredAtDTUPay() {
        customerId = userManagementAdapter.registerCustomer(customer.getFirstName(), customer.getLastName(), customer.getCprNumber(), customerAccountId);
        assertNotNull(customerId);
    }

    @Given("the customer with name {string} {string} and CPR {string} has no bank account")
    public void theCustomerWithNameAndCPRHasNoBankAccount(String firstName, String lastName, String cprNumber) {
        this.customer = new User();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCprNumber(cprNumber);
        this.customerAccountId = null;
    }

    @And("the received error message is {string}")
    public void theReceivedErrorMessageIs(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
