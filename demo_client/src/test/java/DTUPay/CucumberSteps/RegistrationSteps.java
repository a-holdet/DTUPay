package DTUPay.CucumberSteps;

import CustomerMobileApp.UserManagementAdapter;
import DTUPay.Holders.CustomerHolder;
import DTUPay.Holders.ExceptionHolder;
import DTUPay.Holders.MerchantHolder;
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
    private final UserHolder customerHolder;
    private final UserHolder merchantHolder;
    ExceptionHolder exceptionHolder;

    public RegistrationSteps(CustomerHolder customerHolder, MerchantHolder merchantHolder, ExceptionHolder exceptionHolder) {
        this.customerHolder = customerHolder;
        this.merchantHolder = merchantHolder;
        this.exceptionHolder = exceptionHolder;
    }

    @After
    public void after(){
        try {
            if (customerHolder.getAccountId() != null)
                bankService.retireAccount(customerHolder.getAccountId());
        } catch (BankServiceException_Exception e) {
        }
        try {
            if (merchantHolder.getAccountId() != null)
                bankService.retireAccount(merchantHolder.getAccountId());
        } catch (BankServiceException_Exception e) {
        }

        //TODO should we retire DTUPay account?
        customerHolder.reset();
        merchantHolder.reset();
    }

    @And("the customer is registered with DTUPay")
    public void theCustomerIsRegisteredWithDTUPay() {
        try {
            customerHolder.setId(userManagementAdapter.registerCustomer(customerHolder.getFirstName(), customerHolder.getLastName(), customerHolder.getCpr(), customerHolder.getAccountId()));
            assertNotNull(customerHolder.getId());
        } catch (IllegalArgumentException e){
            exceptionHolder.setException(e);
            customerHolder.setId(null);
        }
    }

    private int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private void setCustomerHolderBasics(){
        int lastFour = getRandomNumberInRange(1000,9999);
        String cpr = "200167-"+ lastFour;

        customerHolder.setFirstName("Stein");
        customerHolder.setLastName("Bagger");
        customerHolder.setCpr(cpr);
    }

    private void setMerchantHolderBasics(){
        int lastFour = getRandomNumberInRange(1000,9999);
        String cpr = "150363-"+ lastFour;

        merchantHolder.setFirstName("Joe");
        merchantHolder.setLastName("Exotic");
        merchantHolder.setCpr(cpr);
    }

    @Given("the customer has a bank account")
    public void theCustomerHasABankAccount() {
        setCustomerHolderBasics();
        User customerBank = new User();
        customerBank.setFirstName(customerHolder.getFirstName());
        customerBank.setLastName(customerHolder.getLastName());
        customerBank.setCprNumber(customerHolder.getCpr());
        try {
            customerHolder.setAccountId(bankService.createAccountWithBalance(customerBank, new BigDecimal(1000)));
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }

    @And("the merchant has a bank account")
    public void theMerchantHasABankAccount() {
        setMerchantHolderBasics();
        User merchantBank = new User();
        merchantBank.setFirstName(merchantHolder.getFirstName());
        merchantBank.setLastName(merchantHolder.getLastName());
        merchantBank.setCprNumber(merchantHolder.getCpr());
        try {
            merchantHolder.setAccountId(bankService.createAccountWithBalance(merchantBank, new BigDecimal(2000)));
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @And("the merchant is registered with DTUPay")
    public void theMerchantIsRegisteredWithDTUPay() {
        merchantHolder.setId(userManagementAdapter.registerMerchant(merchantHolder.getFirstName(), merchantHolder.getLastName(), merchantHolder.getCpr(), merchantHolder.getAccountId()));
    }

    @And("the merchant is not registered with DTUPay")
    public void theMerchantIsNotRegisteredWithDTUPay() {
        //Do not register merchant with DTUPay
    }

    @Then("the registration is not successful")
    public void theRegistrationIsNotSuccessful() {
        assertNull(customerHolder.getId());
    }

    @And("the error message is {string}")
    public void theErrorMessageIs(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, exceptionHolder.getException().getMessage());
    }

    @Given("the customer has no bank account")
    public void theCustomerWithNameAndCPRHasNoBankAccount() {
        setCustomerHolderBasics();
        //Do not create account
    }


    @And("another merchant has a bank account")
    public void anotherMerchantHasABankAccount() {
    }
}
