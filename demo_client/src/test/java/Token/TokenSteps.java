package Token;

import CustomerMobileApp.DTUPay;
import CustomerMobileApp.TokenGenerationAdapter;
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
    DTUPay dtuPay;
    TokenGenerationAdapter tokenService;
    List<UUID> tokens;
    BankService bankService;
    UnauthorizedException unauthorizedException;

    @Before
    public void setup() {
        this.bankService = new BankServiceService().getBankServicePort();
        this.tokenService = new TokenGenerationAdapter();
        this.tokens = new ArrayList<>();
        this.dtuPay = new DTUPay();
    }

    @After
    public void teardown() {
        if (customer != null) dtuPay.deleteTokensFor(customer);
        try {
            if (customerAccountId != null) bankService.retireAccount(customerAccountId);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            fail();
        }
        customerAccountId = null;
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
        tokenService.createTokensForCustomer(customer, tokenAmount);
        int amount = tokenService.readTokensForCustomer(customer).size();
        assertEquals(tokenAmount, amount);
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int tokenAmount) {
        try {
            tokens = dtuPay.requestNewTokens(customer, tokenAmount);
        } catch (UnauthorizedException e) {
            this.unauthorizedException = e;
        }
    }

    @Then("the customer gets {int} tokens")
    public void theCustomerGetsTokens(int tokenAmount) {
        assertEquals(tokenAmount, tokens.size());
    }

    @Then("the token granting is not successful")
    public void theTokenGrantingIsNotSuccessful() {
        assertNotNull(unauthorizedException);
    }

    @Then("the token granting is denied")
    public void theTokenGrantingIsDenied() {

    }

    @And("the customer is registered at DTUPay")
    public void theCustomerIsRegisteredAtDTUPay() {
        dtuPay.registerCustomer(customer, customerAccountId);
    }

    @Given("the customer with name {string} {string} and CPR {string} has no bank account")
    public void theCustomerWithNameAndCPRHasNoBankAccount(String firstName, String lastName, String cprNumber) {
        this.customer = new User();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCprNumber(cprNumber);
        this.customerAccountId = null;
    }
}
