package DTUPay.CucumberSteps;

import CustomerMobileApp.CustomerPort;
import DTUPay.Holders.CustomerHolder;
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

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class TokenSteps {

    //Adapters
    BankService bankService;
    CustomerPort customerPort;

    //Holders
    private final TokenHolder tokenHolder;
    private final UserHolder customerHolder;
    ExceptionHolder exceptionHolder;

    public TokenSteps(TokenHolder tokenHolder, CustomerHolder customerHolder, ExceptionHolder exceptionHolder) {
        this.tokenHolder = tokenHolder;
        this.customerHolder = customerHolder;
        this.exceptionHolder = exceptionHolder;
    }

    @Before
    public void setup() {
        this.customerPort = new CustomerPort();
        this.bankService = new BankServiceService().getBankServicePort();
    }

    @After
    public void teardown() {
        this.customerPort.close();
        if (customerHolder.getId() != null) {
            tokenHolder.reset();
        }
    }
    //Given
    @And("the customer has {int} tokens")
    public void theCustomerHasTokens(int tokenAmount) {
        assertEquals(tokenAmount, tokenHolder.getTokens().size());
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int tokenAmount) {
        try {
            List<UUID> uuids = customerPort.createTokensForCustomer(customerHolder.getId(), tokenAmount);
            tokenHolder.setTokens(uuids);
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
