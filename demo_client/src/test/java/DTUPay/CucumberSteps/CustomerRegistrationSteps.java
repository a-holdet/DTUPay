package DTUPay.CucumberSteps;

import CustomerMobileApp.CustomerAdapter;
import DTUPay.Holders.CustomerHolder;
import DTUPay.Holders.ExceptionHolder;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CustomerRegistrationSteps {
    //Adapters
    BankService bankService = new BankServiceService().getBankServicePort();
    CustomerAdapter customerAdapter;

    //Holders
    private final CustomerHolder customerHolder;
    ExceptionHolder exceptionHolder;

    public CustomerRegistrationSteps(CustomerHolder customerHolder, ExceptionHolder exceptionHolder) {
        this.customerHolder = customerHolder;
        this.exceptionHolder = exceptionHolder;
    }

    @Before
    public void before() {
        customerAdapter = new CustomerAdapter();
    }

    @After
    public void after() {
        customerAdapter.close();
        try {
            if (customerHolder.getAccountId() != null)
                bankService.retireAccount(customerHolder.getAccountId());
        } catch (BankServiceException_Exception e) {
        }
        customerHolder.reset();
    }

    @Given("the customer has a bank account")
    public void theCustomerHasABankAccount() throws BankServiceException_Exception {
        customerHolder.setCustomerBasics();
        User customerBank = new User();
        customerBank.setFirstName(customerHolder.getFirstName());
        customerBank.setLastName(customerHolder.getLastName());
        customerBank.setCprNumber(customerHolder.getCpr());

        customerHolder.setAccountId(bankService.createAccountWithBalance(customerBank, new BigDecimal(1000)));
    }

    @Given("the customer has no bank account")
    public void theCustomerWithNameAndCPRHasNoBankAccount() {
        customerHolder.setCustomerBasics();
        //Do not create account
    }

    @And("the customer is registering with DTUPay")
    public void theCustomerIsRegisteringWithDTUPay() {
        try {
            String customerId = customerAdapter.registerCustomer(customerHolder.getFirstName(), customerHolder.getLastName(), customerHolder.getCpr(), customerHolder.getAccountId());
            customerHolder.setId(customerId);
        } catch (IllegalArgumentException e) {
            exceptionHolder.setException(e);
            customerHolder.setId(null);
        }
    }

    @Then("the customer registration is successful")
    public void theRegistrationIsSuccessful() {
        assertNotNull(customerHolder.getId());
    }

    @Then("the customer registration is not successful")
    public void theRegistrationIsNotSuccessful() {
        assertNull(customerHolder.getId());
    }

    @And("the error message is {string}")
    public void theErrorMessageIs(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, exceptionHolder.getException().getMessage());
    }
}