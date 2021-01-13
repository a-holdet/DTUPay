package DTUPay;

import CustomerMobileApp.DTUPay;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import dtu.ws.fastmoney.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PaymentServiceSteps {

    @Before
    public void beforeScenario() {
        Account acc1 = null;
        try {
            acc1 = bankService.getAccountByCprNumber("290276-1234");
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

    User customer;
    User merchant;
    BankService bankService = new BankServiceService().getBankServicePort();
    String customerAccountId;
    String merchantAccountId;
    String mostRecentAccountId;
    String customerId;
    String merchantId;
    DTUPay dtuPay = new DTUPay();
    boolean successful;

    @Given("the customer {string} {string} with CPR {string} has a bank account")
    public void theCustomerWithCPRHasABankAccount(String firstName, String lastName, String cpr) {
        customer = new User();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCprNumber(cpr);
        try {
            customerAccountId = bankService.createAccountWithBalance(customer, new BigDecimal(1000));
            mostRecentAccountId = customerAccountId;
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
        customerId = dtuPay.registerCustomer(customer, customerAccountId);
    }

    @And("the merchant {string} {string} with CPR {string} has a bank account")
    public void theMerchantWithCPRHasABankAccount(String firstName, String lastName, String cpr) {
        merchant = new User();
        merchant.setFirstName(firstName);
        merchant.setLastName(lastName);
        merchant.setCprNumber(cpr);
        try {
            merchantAccountId = bankService.createAccountWithBalance(merchant, new BigDecimal(2000));
            mostRecentAccountId = merchantAccountId;
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @And("the merchant is registered with DTUPay")
    public void theMerchantIsRegisteredWithDTUPay() {
        merchantId = dtuPay.registerMerchant(merchant, merchantAccountId);
    }

    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
        try {
            dtuPay.transferMoneyFromTo(customerAccountId,merchantId,new BigDecimal(amount),"myscription");
            successful=true;
        } catch (Exception e) {
            successful = false;
        }
    }

    @And("the balance of the customer at the bank is {int} kr")
    public void theBalanceOfTheCustomerAtTheBankIsKr(int expectedBalance) {
        try {
            Account account = bankService.getAccount(customerAccountId);
            assertEquals(new BigDecimal(expectedBalance), account.getBalance());
        } catch (BankServiceException_Exception e) {
            fail("Wrong balance for customer");
        }
    }

    @And("the balance of the merchant at the bank is {int} kr")
    public void theBalanceOfTheMerchantAtTheBankIsKr(int expectedBalance) {
        try {
            Account account = bankService.getAccount(merchantAccountId);
            assertEquals(new BigDecimal(expectedBalance), account.getBalance());
        } catch (BankServiceException_Exception e) {
            fail("Wrong balance for merchant");
        }
    }

    @After
    public void afterScenario() {
        try {
            bankService.retireAccount(customerAccountId);
        } catch (BankServiceException_Exception e) {
            // e.printStackTrace();
        }
        try {
            bankService.retireAccount(merchantAccountId);
        } catch (BankServiceException_Exception e) {
            // ½ e.printStackTrace();
        }
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertTrue(successful);
        ;
    }
}
