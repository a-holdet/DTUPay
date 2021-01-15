package DTUPay.CucumberSteps;

import CustomerMobileApp.CustomerAdapter;
import CustomerMobileApp.MerchantAdapter;
import DTUPay.Holders.CustomerHolder;
import DTUPay.Holders.ExceptionHolder;
import DTUPay.Holders.MerchantHolder;
import DTUPay.Holders.UserHolder;
import DTUPay.Holders.*;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class RegistrationSteps {
    //Adapters
    BankService bankService = new BankServiceService().getBankServicePort();
    CustomerAdapter customerAdapter = new CustomerAdapter();
    MerchantAdapter merchantAdapter = new MerchantAdapter();

    //Holders
    private final CustomerHolder customerHolder;
    private final MerchantHolder merchantHolder;
    private final OtherMerchantHolder otherMerchantHolder;
    ExceptionHolder exceptionHolder;

    public RegistrationSteps(CustomerHolder customerHolder, MerchantHolder merchantHolder, OtherMerchantHolder otherMerchantHolder, ExceptionHolder exceptionHolder) {
        this.customerHolder = customerHolder;
        this.merchantHolder = merchantHolder;
        this.otherMerchantHolder = otherMerchantHolder;
        this.exceptionHolder = exceptionHolder;
    }

    @After
    public void after() {
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

        customerHolder.reset();
        merchantHolder.reset();
        otherMerchantHolder.reset();
    }

    @And("the customer is registered with DTUPay")
    public void theCustomerIsRegisteredWithDTUPay() {
        try {
            customerHolder.setId(customerAdapter.registerCustomer(customerHolder.getFirstName(), customerHolder.getLastName(), customerHolder.getCpr(), customerHolder.getAccountId()));
            assertNotNull(customerHolder.getId());
        } catch (IllegalArgumentException e) {
            exceptionHolder.setException(e);
            customerHolder.setId(null);
        }
    }

    @Given("the customer has a bank account")
    public void theCustomerHasABankAccount() {
        customerHolder.setCustomerBasics();
        User customerBank = new User();
        customerBank.setFirstName(customerHolder.getFirstName());
        customerBank.setLastName(customerHolder.getLastName());
        customerBank.setCprNumber(customerHolder.getCpr());

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
    public void theMerchantHasABankAccount() throws BankServiceException_Exception {
        merchantHolder.setMerchantBasics();
        User merchantBank = new User();
        merchantBank.setFirstName(merchantHolder.getFirstName());
        merchantBank.setLastName(merchantHolder.getLastName());
        merchantBank.setCprNumber(merchantHolder.getCpr());

        merchantHolder.setAccountId(bankService.createAccountWithBalance(merchantBank, new BigDecimal(2000)));
    }

    @And("the merchant is registered with DTUPay")
    public void theMerchantIsRegisteredWithDTUPay() {
        registerMerchantWithDTUPay(merchantHolder);
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
        customerHolder.setCustomerBasics();
        //Do not create account
    }


    @And("another merchant has a bank account")
    public void anotherMerchantHasABankAccount() throws BankServiceException_Exception {
        otherMerchantHolder.setMerchantBasics();
        User merchantBank = new User();
        merchantBank.setFirstName(otherMerchantHolder.getFirstName());
        merchantBank.setLastName(otherMerchantHolder.getLastName());
        merchantBank.setCprNumber(otherMerchantHolder.getCpr());

        otherMerchantHolder.setAccountId(bankService.createAccountWithBalance(merchantBank, new BigDecimal(2000)));
    }

    @And("the other merchant is registered with DTUPay")
    public void theOtherMerchantIsRegisteredWithDTUPay() {
        registerMerchantWithDTUPay(otherMerchantHolder);
    }

    private void registerMerchantWithDTUPay(UserHolder merchantHolder) {
        merchantHolder.setId(merchantAdapter.registerMerchant(merchantHolder.getFirstName(), merchantHolder.getLastName(), merchantHolder.getCpr(), merchantHolder.getAccountId()));
    }
}
