package DTUPay.CucumberSteps;

import CustomerMobileApp.*;
import DTUPay.Holders.TokenHolder;
import DTUPay.Holders.UserHolder;
import dtu.ws.fastmoney.User;
import io.cucumber.gherkin.Token;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class ReportingSteps {

    // Holders
    UserHolder customer = UserHolder.customer;
    UserHolder merchant = UserHolder.merchant;
    TokenHolder tokenHolder = TokenHolder.instance;

    // Adapters
    PaymentAdapter paymentAdapter = new PaymentAdapter();
    TokenGenerationAdapter tokenGenerationAdapter = new TokenGenerationAdapter();
    ReportingAdapter reportingAdapter = new ReportingAdapter();

    Report report;
    UUID tokenUsedInPayment;

    @And("the merchant and customer perform a successful payment of {int} kr for a {string}")
    public void theMerchantAndCustomerPerformASuccessfulPaymentOfKrForA(int amount, String productDescription) {
        try {
            tokenHolder.setTokens(tokenGenerationAdapter.createTokensForCustomer(customer.id, 1)); // Request 1 token
            tokenUsedInPayment = tokenHolder.getTokens().get(0); // Extract the token
            paymentAdapter.transferMoneyFromTo(tokenUsedInPayment, merchant.id, BigDecimal.valueOf(amount), productDescription); // Make payment
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("the merchant requests a report of transactions")
    public void theMerchantRequestsAReportOfTransactions() {
        report = reportingAdapter.getReportFor(merchant.id);
        assertNotNull(report);
    }

    @Then("the merchant receives a report having a transaction of {int} kr for a {string} to the merchant using the same token")
    public void theMerchantReceivesAReportHavingATransactionOfKrForAToTheMerchantUsingTheSameToken(int amount, String productDescription) {
        // check merchant is correct
        assertEquals(merchant.firstName, report.getMerchant().getFirstName());
        assertEquals(merchant.lastName, report.getMerchant().getLastName());
        assertEquals(merchant.cpr, report.getMerchant().getCprNumber());

        // Check transactions is correct
        boolean foundCorrectTransaction = report.getPayments().stream().anyMatch(payment ->
                payment.customerToken.equals(tokenUsedInPayment) &&
                        payment.amount.equals(BigDecimal.valueOf(amount)) &&
                        payment.description.equals(productDescription)
        );
        assertTrue(foundCorrectTransaction);
        assertEquals(report.getPayments().size(), 1);
    }
}
