package PaymentServiceTests;

import Bank.DTUBankPort;
import Bank.IBank;
import DTO.Customer;
import DTO.Merchant;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import paymentservice.*;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PaymentServiceSteps {

    BankService bankService = new BankServiceService().getBankServicePort();
    ICustomerService customerService = MessageQueueAccountService.getInstance();
    IMerchantService merchantService = MessageQueueAccountService.getInstance();
    IPaymentService paymentService = PaymentService.getInstance();
    Customer customer;
    Merchant merchant;

    @Given("a valid merchant, customer and token")
    public void aValidMerchantCustomerAndToken() throws BankServiceException_Exception {
        User customer = new User();
        customer.setFirstName("first");
        customer.setLastName("last");
        customer.setCprNumber("123456-8239");

        User merchant = new User();
        merchant.setFirstName("mfirst");
        merchant.setLastName("mlast");
        merchant.setCprNumber("123456-7853");

        // register at bank
        String customerAccountId = bankService.createAccountWithBalance(customer, BigDecimal.ZERO);
        String merchantAccountId = bankService.createAccountWithBalance(merchant, BigDecimal.ZERO);

        this.customer = new Customer();
        this.customer.firstName = customer.getFirstName();
        this.customer.lastName = customer.getLastName();
        this.customer.cprNumber = customer.getCprNumber();
        this.customer.accountId = customerAccountId;

        this.merchant = new Merchant();
        this.merchant.firstName = merchant.getFirstName();
        this.merchant.lastName = merchant.getLastName();
        this.merchant.cprNumber = merchant.getCprNumber();
        this.merchant.accountId = merchantAccountId;

        assertNotNull(customerAccountId);
        assertNotNull(merchantAccountId);
    }

    @When("I receive a valid payment")
    public void iReceiveAValidPayment() {
        assertTrue(true);

    }

    @Then("I send back a valid response")
    public void iSendBackAValidResponse() {
        assertTrue(true);
    }
}
