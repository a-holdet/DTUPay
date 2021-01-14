package DTUPay.CucumberSteps;

import CustomerMobileApp.*;
import DTUPay.Holders.MerchantHolder;
import DTUPay.Holders.OtherMerchantHolder;
import DTUPay.Holders.TokenHolder;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ReportingSteps {

    // Holders
    private MerchantHolder merchant;
    private OtherMerchantHolder otherMerchant;
    TokenHolder tokenHolder;

    public ReportingSteps(MerchantHolder merchant, OtherMerchantHolder otherMerchant, TokenHolder tokenHolder) {
        this.merchant = merchant;
        this.otherMerchant = otherMerchant;
        this.tokenHolder = tokenHolder;
    }

    // Adapters
    ReportingAdapter reportingAdapter = new ReportingAdapter();

    Report report;

    @When("the merchant requests a report of transactions")
    public void theMerchantRequestsAReportOfTransactions() {
        report = reportingAdapter.getReportFor(merchant.getId());
        assertNotNull(report);
    }

    @Then("the merchant receives a report having a transaction of {int} kr for a {string} to the merchant using the same token")
    public void theMerchantReceivesAReportHavingATransactionOfKrForAToTheMerchantUsingTheSameToken(int amount, String productDescription) {
        // check merchant is correct
        assertEquals(merchant.getFirstName(), report.getMerchant().getFirstName());
        assertEquals(merchant.getLastName(), report.getMerchant().getLastName());
        assertEquals(merchant.getCpr(), report.getMerchant().getCprNumber());

        // Check transactions is correct
        boolean foundCorrectTransaction = report.getPayments().stream().anyMatch(payment ->
                payment.customerToken.equals(tokenHolder.getTokens().get(0)) &&
                payment.amount.equals(BigDecimal.valueOf(amount)) &&
                payment.description.equals(productDescription)
        );
        assertTrue(foundCorrectTransaction);
        assertEquals(report.getPayments().size(), 1);
    }

    @Then("he does not see the other merchants report")
    public void heDoesNotSeeTheOtherMerchantsReport() {
        boolean foundOtherMerchantPaymentsInReport = report.getPayments().stream().anyMatch(payment ->
                payment.merchantId.equals(otherMerchant.getId())
        );
        assertFalse(foundOtherMerchantPaymentsInReport);
    }
}
