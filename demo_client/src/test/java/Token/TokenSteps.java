package Token;

import CustomerMobileApp.DTUPay;
import CustomerMobileApp.TokenGenerationAdapter;
import CustomerMobileApp.ITokenGeneration;
import CustomerMobileApp.Customer;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TokenSteps {

    Customer customer;
    DTUPay dtuPay;
    ITokenGeneration tokenService;
    List<UUID> tokens;

    @BeforeAll
    public void setup() {
        this.tokenService = new TokenGenerationAdapter();
        this.tokens = new ArrayList<>();
    }

    @Given("the customer with name {string} {string} and CPR {string} has a bank account")
    public void theCustomerWithNameAndCPRHasABankAccount(String firstName, String lastName, String cpr) {
        this.customer = new Customer(firstName, lastName, cpr);
        this.dtuPay = new DTUPay();
    }

    @And("the customer has {int} tokens")
    public void theCustomerHasTokens(int tokenAmount) {
        tokenService.createTokensForCustomer(customer, tokenAmount);
        int amount = tokenService.readTokensForCustomer(customer).size();
        assertEquals(tokenAmount, amount);
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int tokenAmount) {
        dtuPay.requestNewTokens(customer, tokenAmount);
    }

    @Then("the customer gets {int} tokens")
    public void theCustomerGetsTokens(int tokenAmount) {
        assertEquals(tokens.size(), tokenAmount);
    }

    @Given("the customer {string} {string} with CPR {string} has no bank account")
    public void theCustomerWithCPRHasNoBankAccount(String arg0, String arg1, String arg2) {
    }

    @Then("the token granting is not successful")
    public void theTokenGrantingIsNotSuccessful() {
    }

    @Then("the token granting is denied")
    public void theTokenGrantingIsDenied() {
    }
}
