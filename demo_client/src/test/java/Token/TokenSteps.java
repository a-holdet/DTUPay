package Token;

import CustomerMobileApp.DTUPay;
import CustomerMobileApp.TokenGenerationAdapter;
import CustomerMobileApp.ITokenGeneration;
import CustomerMobileApp.Customer;
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
import org.junit.jupiter.api.BeforeAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class TokenSteps {

    Customer customer;
    DTUPay dtuPay;
    ITokenGeneration tokenService;
    List<UUID> tokens;
    BankService bankService;

    @Before
    public void setup() {
        this.bankService = new BankServiceService().getBankServicePort();
        this.tokenService = new TokenGenerationAdapter();
        this.tokens = new ArrayList<>();
        this.dtuPay = new DTUPay();
    }

    @After
    public void teardown() {
        dtuPay.deleteTokensFor(customer);
    }

    @Given("the customer with name {string} {string} and CPR {string} has a bank account")
    public void theCustomerWithNameAndCPRHasABankAccount(String firstName, String lastName, String cpr) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCprNumber(cpr);
        String accountId;
        try {
            accountId = bankService.createAccountWithBalance(user, BigDecimal.ZERO);
            this.customer = new Customer(firstName, lastName, cpr, accountId);
        } catch (BankServiceException_Exception e) {
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
        tokens = dtuPay.requestNewTokens(customer, tokenAmount);
    }

    @Then("the customer gets {int} tokens")
    public void theCustomerGetsTokens(int tokenAmount) {
        assertEquals(tokenAmount, tokens.size());
    }

    @Then("the token granting is not successful")
    public void theTokenGrantingIsNotSuccessful() {
    }

    @Then("the token granting is denied")
    public void theTokenGrantingIsDenied() {
    }

    @And("the customer is registered at DTUPay")
    public void theCustomerIsRegisteredAtDTUPay() {
        dtuPay.registerCustomer(customer);
    }

    @Given("the customer with name {string} {string} and CPR {string} has no bank account")
    public void theCustomerWithNameAndCPRHasNoBankAccount(String firstName, String lastName, String cprNumber) {
        this.customer = new Customer(firstName, lastName, cprNumber, null);
    }
}
