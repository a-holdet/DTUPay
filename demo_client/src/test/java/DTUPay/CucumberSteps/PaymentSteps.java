package DTUPay.CucumberSteps;

import CustomerMobileApp.MerchantAdapter;
import DTUPay.Holders.CustomerHolder;
import DTUPay.Holders.TokenHolder;
import DTUPay.Holders.UserHolder;
import CustomerMobileApp.PaymentAdapter;
import CustomerMobileApp.TokenGenerationAdapter;
import DTUPay.Holders.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import dtu.ws.fastmoney.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class PaymentSteps {

    //Adapters
    BankService bankService = new BankServiceService().getBankServicePort();
    MerchantAdapter merchantAdapter = new MerchantAdapter();

    PaymentAdapter paymentAdapter = new PaymentAdapter();
    TokenGenerationAdapter tokenGenerationAdapter = new TokenGenerationAdapter();

    //Holders
    private final TokenHolder tokenHolder;
    private final CustomerHolder customerHolder;
    private final MerchantHolder merchantHolder;
    private final OtherMerchantHolder otherMerchantHolder;

    //Class specifics
    UUID selectedToken;
    String errorMessage;
    //String mostRecentAccountId;
    boolean successful;

    public PaymentSteps(TokenHolder tokenHolder, CustomerHolder customerHolder, MerchantHolder merchantHolder, OtherMerchantHolder otherMerchantHolder) {
        this.tokenHolder = tokenHolder;
        this.customerHolder = customerHolder;
        this.merchantHolder = merchantHolder;
        this.otherMerchantHolder = otherMerchantHolder;
    }

    @Before
    public void beforeScenario() {
        Account acc1 = null;
        try {
            acc1 = bankService.getAccountByCprNumber("290276-7777");
            bankService.retireAccount(acc1.getId());
        } catch (BankServiceException_Exception e) {
        }

        Account acc2 = null;
        try {
            acc2 = bankService.getAccountByCprNumber("207082-0101");
            bankService.retireAccount(acc2.getId());
        } catch (BankServiceException_Exception e) {
        }
    }

    @After
    public void afterScenario() {
        customerHolder.reset();
        merchantHolder.reset();
        otherMerchantHolder.reset();
        tokenHolder.reset();
    }


    @And("the balance of the customer account is {int}")
    public void theBalanceOfTheCustomerAccountIs(int expectedBalance) {
        try {
            Account account = bankService.getAccount(customerHolder.getAccountId());
            assertEquals(new BigDecimal(expectedBalance), account.getBalance());
        } catch (BankServiceException_Exception e) {
            fail();
        }
    }

    @And("the balance of the merchant account is {int}")
    public void theBalanceOfTheMerchantAccountIs(int expectedBalance) {
        try {
            Account account = bankService.getAccount(merchantHolder.getAccountId());
            assertEquals(new BigDecimal(expectedBalance), account.getBalance());
        } catch (BankServiceException_Exception e) {
            fail();
        }
    }

    @When("the merchant initiates a payment for {int} kr using the selected customer token")
    public void theMerchantInitiatesAPaymentForKrUsingTheSelectedCustomerToken(int amount) {
        try {
            merchantAdapter.transferMoneyFromTo(selectedToken,merchantHolder.getId(),new BigDecimal(amount),"myscription");
            successful=true;
        } catch (Exception e) {
            e.printStackTrace();
            successful = false;
            errorMessage=e.getMessage();
        }
    }

    @And("the balance of the customer at the bank is {int} kr")
    public void theBalanceOfTheCustomerAtTheBankIsKr(int expectedBalance) {
        try {
            Account account = bankService.getAccount(customerHolder.getAccountId());
            assertEquals(new BigDecimal(expectedBalance), account.getBalance());
        } catch (BankServiceException_Exception e) {
            fail("Wrong balance for customer");
        }
    }

    @And("the balance of the merchant at the bank is {int} kr")
    public void theBalanceOfTheMerchantAtTheBankIsKr(int expectedBalance) {
        try {
            Account account = bankService.getAccount(merchantHolder.getAccountId());
            assertEquals(new BigDecimal(expectedBalance), account.getBalance());
        } catch (BankServiceException_Exception e) {
            fail("Wrong balance for merchant");
        }
    }


    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertTrue(successful);
    }

    @And("the customer selects a token")
    public void theCustomerSelectsAToken() {
        selectedToken = tokenHolder.getTokens().get(0);
    }


    @And("the customer selects a non-valid token")
    public void theCustomerSelectsANonValidToken() {
        selectedToken = UUID.randomUUID();
    }

    @Then("the payment fails")
    public void thePaymentFails() {
        assertFalse(successful);
    }

    @And("the other merchant and customer perform a successful payment of {int} kr for a {string}")
    public void theOtherMerchantAndCustomerPerformASuccessfulPaymentOfKrForA(int amount, String productDescription) {
        UUID nextTokenUsedInPayment = tokenHolder.getTokens().get(1); // Extract 2nd token
        performPaymentUsing(nextTokenUsedInPayment, otherMerchantHolder, amount, productDescription);
    }

    @And("the merchant and customer perform a successful payment of {int} kr for a {string}")
    public void theMerchantAndCustomerPerformASuccessfulPaymentOfKrForA(int amount, String productDescription) {
        try {
            tokenHolder.setTokens(tokenGenerationAdapter.createTokensForCustomer(customerHolder.getId(), 2)); // Request 2 tokens
            UUID tokenUsedInPayment = tokenHolder.getTokens().get(0); // Extract 1st token
            performPaymentUsing(tokenUsedInPayment, merchantHolder, amount, productDescription);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void performPaymentUsing(UUID token, UserHolder merchantHolder, int amount, String productDescription) {
        try {
            System.out.println("PERFORMING PAYMENT:" + merchantHolder.getId() + ". " + productDescription);
            paymentAdapter.transferMoneyFromTo(token, merchantHolder.getId(), BigDecimal.valueOf(amount), productDescription); // Make payment
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
