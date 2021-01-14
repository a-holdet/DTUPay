package DTUPay.CucumberSteps;

import CustomerMobileApp.CustomerAdapter;
import DTUPay.Holders.ExceptionHolder;
import DTUPay.Holders.TokenHolder;
import DTUPay.Holders.UserHolder;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class TokenSteps {

    //Adapters
    BankService bankService;
    CustomerAdapter customerAdapter = new CustomerAdapter();
    //MerchantAdapter merchantAdapter = new MerchantAdapter();

    //Holders
    TokenHolder tokenHolder = TokenHolder.instance;
    UserHolder customerHolder = UserHolder.customer;
    ExceptionHolder exceptionHolder;

    public TokenSteps(ExceptionHolder exceptionHolder) {
        this.exceptionHolder = exceptionHolder;
    }

    @Before
    public void setup() {
        this.bankService = new BankServiceService().getBankServicePort();
    }

    @After
    public void teardown() {
        if (customerHolder.id != null) {
            tokenHolder.reset();
        }
    }

    @And("the customer has {int} tokens")
    public void theCustomerHasTokens(int tokenAmount) {
        assertEquals(tokenAmount, tokenHolder.tokens.size());
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int tokenAmount) {
        try {
            tokenHolder.setTokens(customerAdapter.createTokensForCustomer(customerHolder.id, tokenAmount));
        } catch (Exception e) {
            this.exceptionHolder.setException(e);
        }
    }

    @Then("the customer gets {int} tokens")
    public void theCustomerGetsTokens(int tokenAmount) {
        assertEquals(tokenAmount, tokenHolder.getTokens().size());
    }

    @Then("the token granting is not successful")
    public void theTokenGrantingIsNotSuccessful() {
        assertNotNull(exceptionHolder.getException());
    }

    @Then("the token granting is denied")
    public void theTokenGrantingIsDenied() {
    }

    @And("the received error message is {string}")
    public void theReceivedErrorMessageIs(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, exceptionHolder.getException().getMessage());
    }
}
