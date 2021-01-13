package DTUPay;

import CustomerMobileApp.UserManagementAdapter;
import dtu.ws.fastmoney.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class RegistrationSteps {
    //Adapters
    UserManagementAdapter userManagementAdapter = new UserManagementAdapter();
    //Holders
    UserHolder customerHolder = UserHolder.customer;
    //Class specifics
    String errorMessage;

    @Given("the customer {string} {string} with CPR {string} does not have a bank account")
    public void theCustomerWithCPRDoesNotHaveABankAccount(String firstName, String lastName, String cprNumber) {
        User customer = new User();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCprNumber(cprNumber);
    }

    @When("the customer is registering with DTUPay")
    public void theCustomerIsRegisteringWithDTUPay() {
        try {
            customerHolder.id = userManagementAdapter.registerCustomer(customerHolder.firstName, customerHolder.lastName, customerHolder.cpr,null);
        } catch (IllegalArgumentException e) {
            customerHolder.id = null;
            errorMessage = e.getMessage();
        }
    }

    @Then("the registration is not successful")
    public void theRegistrationIsNotSuccessful() {
        assertNull(customerHolder.id);
    }

    @And("the error message is {string}")
    public void theErrorMessageIs(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, errorMessage);
    }
}
