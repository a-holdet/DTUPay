package DTUPay.CucumberSteps;

import CustomerMobileApp.TokenGenerationAdapter;
import CustomerMobileApp.UserManagementAdapter;
import DTUPay.Holders.ExceptionHolder;
import DTUPay.Holders.TokenHolder;
import DTUPay.Holders.UserHolder;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.quarkus.security.UnauthorizedException;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class TokenSteps {

    //Adapters
    BankService bankService;
    TokenGenerationAdapter tokenAdapter;
    UserManagementAdapter userManagementAdapter;

    //Holders
    TokenHolder tokenHolder = TokenHolder.instance;
    UserHolder customerHolder = UserHolder.customer;
    ExceptionHolder exceptionHolder = ExceptionHolder.instance;

    @Before
    public void setup() {
        this.bankService = new BankServiceService().getBankServicePort();
        this.tokenAdapter = new TokenGenerationAdapter();
        this.userManagementAdapter = new UserManagementAdapter();
    }

    @After
    public void teardown() {
        System.out.println("Hello from token teardown");
        if (customerHolder.id != null) {
            tokenAdapter.deleteTokensFor(customerHolder.id);
            tokenHolder.reset();
        }
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
            tokenHolder.setTokens(tokenAdapter.createTokensForCustomer(customerHolder.id, tokenAmount));
        } catch (Exception e) {
            this.exceptionHolder.exception = e;
        }
    }

    @Then("the customer gets {int} tokens")
    public void theCustomerGetsTokens(int tokenAmount) {
        assertEquals(tokenAmount, tokenHolder.getTokens().size());
    }

    @Then("the token granting is not successful")
    public void theTokenGrantingIsNotSuccessful() {
        assertNotNull(exceptionHolder.exception);
    }

    @Then("the token granting is denied")
    public void theTokenGrantingIsDenied() {

    }

    @And("the received error message is {string}")
    public void theReceivedErrorMessageIs(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, exceptionHolder.exception.getMessage());
    }
}
