package DTUPay.CucumberSteps;

import CustomerMobileApp.CustomerPort;
import CustomerMobileApp.MerchantPort;
import DTUPay.Holders.CustomerHolder;
import DTUPay.Holders.MerchantHolder;
import DTUPay.Holders.TokenHolder;
import DTUPay.Holders.UserHolder;
import DTUPay.Holders.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import dtu.ws.fastmoney.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.ws.rs.ForbiddenException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

/***
 * @Author Benjamin Wrist Lam, s153486
 */

public class PaymentSteps {

    // Adapters
    BankService bankService = new BankServiceService().getBankServicePort();
    MerchantPort merchantPort;
    CustomerPort customerPort;

    // Holders
    private final TokenHolder tokenHolder;
    private final CustomerHolder customerHolder;
    private final MerchantHolder merchantHolder;
    private final OtherMerchantHolder otherMerchantHolder;
    private PurchasesHolder purchasesHolder;

    // Class specifics
    UUID selectedToken;
    String errorMessage;
    // String mostRecentAccountId;
    boolean successful;

    public PaymentSteps(TokenHolder tokenHolder, CustomerHolder customerHolder, MerchantHolder merchantHolder,
            OtherMerchantHolder otherMerchantHolder, PurchasesHolder purchasesHolder) {
        this.tokenHolder = tokenHolder;
        this.customerHolder = customerHolder;
        this.merchantHolder = merchantHolder;
        this.otherMerchantHolder = otherMerchantHolder;
        this.purchasesHolder = purchasesHolder;
    }

    @Before
    public void beforeScenario() {
        customerPort = new CustomerPort();
        merchantPort = new MerchantPort();
    }

    @After
    public void afterScenario() {
        customerPort.close();
        merchantPort.close();

        customerHolder.reset();
        merchantHolder.reset();
        otherMerchantHolder.reset();
        tokenHolder.reset();
        purchasesHolder.reset();
    }

    //Given
    @And("the other merchant and customer perform a successful payment of {int} kr for a {string}")
    public void theOtherMerchantAndCustomerPerformASuccessfulPaymentOfKrForA(int amount, String productDescription) throws Exception {
        purchasesHolder.add(amount, productDescription);
        UUID nextTokenUsedInPayment = tokenHolder.getTokens().get(1); // Extract 2nd token
        performPaymentUsing(nextTokenUsedInPayment, otherMerchantHolder, amount, productDescription);
    }

    @And("the balance of the customer account is {int}")
    public void theBalanceOfTheCustomerAccountIs(int expectedBalance) throws BankServiceException_Exception {
        Account account = bankService.getAccount(customerHolder.getAccountId());
        assertEquals(new BigDecimal(expectedBalance), account.getBalance());
    }

    @And("the merchant and customer perform a successful payment of {int} kr for a {string}")
    public void theMerchantAndCustomerPerformASuccessfulPaymentOfKrForA(int amount, String productDescription) throws Exception {
        this.purchasesHolder.add(amount, productDescription);
        tokenHolder.setTokens(customerPort.createTokensForCustomer(customerHolder.getId(), 2)); // Request 2 tokens
        UUID tokenUsedInPayment = tokenHolder.getTokens().get(0); // Extract 1st token
        performPaymentUsing(tokenUsedInPayment, merchantHolder, amount, productDescription);
    }

    @And("the balance of the merchant account is {int}")
    public void theBalanceOfTheMerchantAccountIs(int expectedBalance) throws BankServiceException_Exception {
        Account account = bankService.getAccount(merchantHolder.getAccountId());
        assertEquals(new BigDecimal(expectedBalance), account.getBalance());
    }

    @And("the customer selects a token")
    public void theCustomerSelectsAToken() {
        selectedToken = tokenHolder.getTokens().get(0);
    }

    @And("the customer selects a non-valid token")
    public void theCustomerSelectsANonValidToken() {
        selectedToken = UUID.randomUUID();
    }

    @When("the merchant initiates a payment for {int} kr using the selected customer token")
    public void theMerchantInitiatesAPaymentForKrUsingTheSelectedCustomerToken(int amount) {
        try {
            merchantPort.transferMoneyFromTo(selectedToken, merchantHolder.getId(), new BigDecimal(amount),
                    "myscription");
            successful = true;
        } catch (IllegalArgumentException | ForbiddenException e) {
            successful = false;
            errorMessage = e.getMessage();
        }
    }

    @Then("the payment fails")
    public void thePaymentFails() {
        assertFalse(successful);
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertTrue(successful);
    }

    @And("the balance of the customer at the bank is {int} kr")
    public void theBalanceOfTheCustomerAtTheBankIsKr(int expectedBalance) throws BankServiceException_Exception {
        Account account = bankService.getAccount(customerHolder.getAccountId());
        assertEquals(new BigDecimal(expectedBalance).toBigInteger(), account.getBalance().toBigInteger());
    }

    @And("the balance of the merchant at the bank is {int} kr")
    public void theBalanceOfTheMerchantAtTheBankIsKr(int expectedBalance) throws BankServiceException_Exception {
        Account account = bankService.getAccount(merchantHolder.getAccountId());
        assertEquals(new BigDecimal(expectedBalance).toBigInteger(), account.getBalance().toBigInteger());
    }

    private void performPaymentUsing(UUID token, UserHolder merchantHolder, int amount, String productDescription) throws IllegalArgumentException, ForbiddenException {
        merchantPort.transferMoneyFromTo(token, merchantHolder.getId(), BigDecimal.valueOf(amount),
        productDescription); // Make payment
    }
}
