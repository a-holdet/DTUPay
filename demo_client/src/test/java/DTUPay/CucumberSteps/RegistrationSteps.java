package DTUPay.CucumberSteps;

import CustomerMobileApp.UserManagementAdapter;
import DTUPay.Holders.ExceptionHolder;
import DTUPay.Holders.UserHolder;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.Assert.*;

public class RegistrationSteps {
    //Adapters
    UserManagementAdapter userManagementAdapter = new UserManagementAdapter();
    BankService bankService = new BankServiceService().getBankServicePort();

    //Holders
    UserHolder customerHolder = UserHolder.customer;
    UserHolder merchantHolder = UserHolder.merchant;
    ExceptionHolder exceptionHolder = ExceptionHolder.instance;

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

    @And("the customer is registered with DTUPay")
    public void theCustomerIsRegisteredWithDTUPay() {
        try {
            customerHolder.id = userManagementAdapter.registerCustomer(customerHolder.firstName, customerHolder.lastName, customerHolder.cpr, customerHolder.accountId);
            assertNotNull(customerHolder.id);
        } catch (IllegalArgumentException e){
            exceptionHolder.exception = e;
            customerHolder.id=null;
        }
    }

    private int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private void setCustomerHolderBasics(){
        int lastFour = getRandomNumberInRange(1000,9999);
        String cpr = "200167-"+ lastFour;

        customerHolder.firstName = "Stein";
        customerHolder.lastName = "Bagger";
        customerHolder.cpr = cpr;
    }

    private void setMerchantHolderBasics(){
        int lastFour = getRandomNumberInRange(1000,9999);
        String cpr = "150363-"+ lastFour;

        merchantHolder.firstName = "Joe";
        merchantHolder.lastName = "Exotic";
        merchantHolder.cpr = cpr;
    }

    @Given("the customer has a bank account")
    public void theCustomerHasABankAccount() {
        setCustomerHolderBasics();
        User customerBank = new User();
        customerBank.setFirstName(customerHolder.firstName);
        customerBank.setLastName(customerHolder.lastName);
        customerBank.setCprNumber(customerHolder.cpr);
        try {
            customerHolder.accountId = bankService.createAccountWithBalance(customerBank, new BigDecimal(1000));
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }

    @And("the merchant has a bank account")
    public void theMerchantHasABankAccount() {
        setMerchantHolderBasics();
        User merchantBank = new User();
        merchantBank.setFirstName(merchantHolder.firstName);
        merchantBank.setLastName(merchantHolder.lastName);
        merchantBank.setCprNumber(merchantHolder.cpr);
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
        assertEquals(expectedErrorMessage, exceptionHolder.exception.getMessage());
    }

    @Given("the customer has no bank account")
    public void theCustomerWithNameAndCPRHasNoBankAccount() {
        setCustomerHolderBasics();
        //Do not create account
    }
}
