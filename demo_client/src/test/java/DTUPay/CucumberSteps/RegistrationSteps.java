package DTUPay.CucumberSteps;

import CustomerMobileApp.UserManagementAdapter;
import DTUPay.Holders.UserHolder;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class RegistrationSteps {
    //Adapters
    UserManagementAdapter userManagementAdapter = new UserManagementAdapter();
    BankService bankService = new BankServiceService().getBankServicePort();

    //Holders
    UserHolder customerHolder = UserHolder.customer;
    UserHolder merchantHolder = UserHolder.merchant;

    //Class specifics
    String errorMessage;

    @After
    public void after(){
        System.out.println("Hey from registration teardown");

        try {
            if (customerHolder.accountId != null)
                bankService.retireAccount(customerHolder.accountId);
        } catch (BankServiceException_Exception e) {
        }
        try {
            if (merchantHolder.accountId != null)
                bankService.retireAccount(merchantHolder.accountId);
        } catch (BankServiceException_Exception e) {
        }

        //TODO should we retire DTUPay account?
        customerHolder.reset();
        merchantHolder.reset();
    }

    @Given("the customer {string} {string} with CPR {string} does not have a bank account")
    public void theCustomerWithCPRDoesNotHaveABankAccount(String firstName, String lastName, String cprNumber) {
        User customer = new User();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCprNumber(cprNumber);
    }

    @And("the customer is registered with DTUPay")
    public void theCustomerIsRegisteredWithDTUPay() {
        try {
            customerHolder.id = userManagementAdapter.registerCustomer(customerHolder.firstName, customerHolder.lastName, customerHolder.cpr, customerHolder.accountId);
            assertNotNull(customerHolder.id);
        } catch (IllegalArgumentException e){
            errorMessage = e.getMessage();
            customerHolder.id=null;
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
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
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
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @And("the merchant is registered with DTUPay")
    public void theMerchantIsRegisteredWithDTUPay() {
        merchantHolder.id = userManagementAdapter.registerMerchant(merchantHolder.firstName, merchantHolder.lastName, merchantHolder.cpr, merchantHolder.accountId);
    }

    @Then("the registration is not successful")
    public void theRegistrationIsNotSuccessful() {
        assertNull(customerHolder.id);
    }

    @And("the error message is {string}")
    public void theErrorMessageIs(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, errorMessage);
    }

    @Given("the customer with name {string} {string} and CPR {string} has no bank account")
    public void theCustomerWithNameAndCPRHasNoBankAccount(String firstName, String lastName, String cprNumber) {
        this.customerHolder.accountId = null;
    }
}
