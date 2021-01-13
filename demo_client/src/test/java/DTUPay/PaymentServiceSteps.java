package DTUPay;

import CustomerMobileApp.PaymentAdapter;
import CustomerMobileApp.UserManagementAdapter;
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

public class PaymentServiceSteps {

    //Adapters
    BankService bankService = new BankServiceService().getBankServicePort();
    UserManagementAdapter userManagementAdapter = new UserManagementAdapter();
    PaymentAdapter paymentAdapter = new PaymentAdapter();
    //Holders
    TokenHolder tokenHolder = TokenHolder.instance;
    UserHolder customerHolder = UserHolder.customer;
    UserHolder merchantHolder = UserHolder.merchant;
    //Class specifics
    UUID selectedToken;
    String mostRecentAccountId;
    boolean successful;

    @Before
    public void beforeScenario() {
        Account acc1 = null;
        try {
            acc1 = bankService.getAccountByCprNumber("290276-7777");
            bankService.retireAccount(acc1.getId());
        } catch (BankServiceException_Exception e) {
            // e.printStackTrace();
        }

        Account acc2 = null;
        try {
            acc2 = bankService.getAccountByCprNumber("207082-0101");
            bankService.retireAccount(acc2.getId());
        } catch (BankServiceException_Exception e) {
            // e.printStackTrace();
        }
    }

    @Given("the customer {string} {string} with CPR {string} has a bank account")
    public void theCustomerWithCPRHasABankAccount(String firstName, String lastName, String cpr) {
        customerHolder.firstName = firstName;
        customerHolder.lastName = lastName;
        customerHolder.cpr = cpr;
        User customerBank = new User();
        customerBank.setFirstName(firstName);
        customerBank.setLastName(lastName);
        customerBank.setCprNumber(cpr);
        try {
            customerHolder.accountId = bankService.createAccountWithBalance(customerBank, new BigDecimal(1000));
            mostRecentAccountId = customerHolder.accountId;
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }

    @And("the balance of that account is {int}")
    public void theBalanceOfThatAccountIs(int expectedBalance) {
        Account account;
        try {
            account = bankService.getAccount(mostRecentAccountId);
            assertEquals(new BigDecimal(expectedBalance), account.getBalance());
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @And("the customer is registered with DTUPay")
    public void theCustomerIsRegisteredWithDTUPay() throws IllegalArgumentException {
        customerHolder.id = userManagementAdapter.registerCustomer(customerHolder.firstName, customerHolder.lastName, customerHolder.cpr, customerHolder.accountId);
        assertNotNull(customerHolder.id);
    }

    @And("the merchant {string} {string} with CPR {string} has a bank account")
    public void theMerchantWithCPRHasABankAccount(String firstName, String lastName, String cpr) {
        merchantHolder.firstName = firstName;
        merchantHolder.lastName = lastName;
        merchantHolder.cpr = cpr;
        User merchantBank = new User();
        merchantBank.setFirstName(firstName);
        merchantBank.setLastName(lastName);
        merchantBank.setCprNumber(cpr);
        try {
            merchantHolder.accountId = bankService.createAccountWithBalance(merchantBank, new BigDecimal(2000));
            mostRecentAccountId = merchantHolder.accountId;
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @And("the merchant is registered with DTUPay")
    public void theMerchantIsRegisteredWithDTUPay() {
        System.out.println("Merchant is registering with DTU Pay");
        merchantHolder.id = userManagementAdapter.registerMerchant(merchantHolder.firstName, merchantHolder.lastName, merchantHolder.cpr, merchantHolder.accountId);
    }


    @When("the merchant initiates a payment for {int} kr using the selected customer token")
    public void theMerchantInitiatesAPaymentForKrUsingTheSelectedCustomerToken(int amount) {
        try {
            paymentAdapter.transferMoneyFromTo(selectedToken,merchantHolder.id,new BigDecimal(amount),"myscription");
            successful=true;
        } catch (Exception e) {
            successful = false;
        }
    }

    @And("the balance of the customer at the bank is {int} kr")
    public void theBalanceOfTheCustomerAtTheBankIsKr(int expectedBalance) {
        try {
            Account account = bankService.getAccount(customerHolder.accountId);
            assertEquals(new BigDecimal(expectedBalance), account.getBalance());
        } catch (BankServiceException_Exception e) {
            fail("Wrong balance for customer");
        }
    }

    @And("the balance of the merchant at the bank is {int} kr")
    public void theBalanceOfTheMerchantAtTheBankIsKr(int expectedBalance) {
        try {
            Account account = bankService.getAccount(merchantHolder.accountId);
            assertEquals(new BigDecimal(expectedBalance), account.getBalance());
        } catch (BankServiceException_Exception e) {
            fail("Wrong balance for merchant");
        }
    }

    @After
    public void afterScenario() {
        try {
            bankService.retireAccount(customerHolder.accountId);
        } catch (BankServiceException_Exception e) {
            // e.printStackTrace();
        }
        try {
            bankService.retireAccount(merchantHolder.accountId);
        } catch (BankServiceException_Exception e) {
            // ½ e.printStackTrace();
        }
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertTrue(successful);
        ;
    }


    @And("the customer selects a token")
    public void theCustomerSelectsAToken() {
        System.out.println("Customer selects token number 0");
        selectedToken = tokenHolder.tokens.get(0);
    }
}
