package DTUPay.CucumberSteps;

import CustomerMobileApp.*;
import CustomerMobileApp.DTO.Transaction;
import CustomerMobileApp.DTO.UserReport;
import DTUPay.Holders.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ReportingSteps {

    // Adapters
    MerchantAdapter merchantAdapter;
    DTUManagerAdapter managerAdapter;
    CustomerAdapter customerAdapter;

    // Holders
    private MerchantHolder merchant;
    private OtherMerchantHolder otherMerchant;
    private TokenHolder tokenHolder;
    private PurchasesHolder purchasesHolder;
    private CustomerHolder customerHolder;
    ExceptionHolder exceptionHolder;
    // Class specifics
    private UserReport report;
    private List<Transaction> managerOverview;

    public ReportingSteps(MerchantHolder merchant, OtherMerchantHolder otherMerchant, TokenHolder tokenHolder,
                          PurchasesHolder purchasesHolder, CustomerHolder customerHolder, ExceptionHolder exceptionHolder) {
        this.merchant = merchant;
        this.otherMerchant = otherMerchant;
        this.tokenHolder = tokenHolder;
        this.purchasesHolder = purchasesHolder;
        this.customerHolder = customerHolder;
        this.exceptionHolder = exceptionHolder;
    }

    @Before
    public void before() {
        merchantAdapter = new MerchantAdapter();
        managerAdapter = new DTUManagerAdapter();
        customerAdapter = new CustomerAdapter();
    }

    @After
    public void after() {
        merchantAdapter.close();
        managerAdapter.close();
        customerAdapter.close();
    }

    @When("the merchant requests a report of transactions")
    public void theMerchantRequestsAReportOfTransactions() {
        try {
            report = merchantAdapter.getMerchantReport(merchant.getId(), null, null);
            assertNotNull(report);
        } catch (IllegalArgumentException e) {
            this.exceptionHolder.setException(e);
        }
    }

    @Then("the merchant receives a report having a transaction of {int} kr for a {string} to the merchant using the same token")
    public void theMerchantReceivesAReportHavingATransactionOfKrForAToTheMerchantUsingTheSameToken(int amount,
                                                                                                   String productDescription) {
        verifyUserReport(merchant, amount, productDescription, tokenHolder.getTokens().get(0));
    }

    @Then("he does not see the other merchants report")
    public void heDoesNotSeeTheOtherMerchantsReport() {
        boolean foundOtherMerchantPaymentsInReport = report.getPayments().stream()
                .anyMatch(payment -> payment.merchantId.equals(otherMerchant.getId()));
        assertFalse(foundOtherMerchantPaymentsInReport);
    }

    @When("DTUPay requests a report of transactions")
    public void dtupayRequestsAReportOfTransactions() {
        managerOverview = managerAdapter.getManagerOverview();
        assertNotNull(managerOverview);
    }

    @Then("DTUPay receives a report including both transactions")
    public void dtupayReceivesAReportIncludingBothTransactions() {
        PurchasesHolder.Purchase p1 = purchasesHolder.getPurchases().get(0);
        PurchasesHolder.Purchase p2 = purchasesHolder.getPurchases().get(1);

        boolean firstProductIsPresent = managerOverview.stream()
                .anyMatch(transaction -> transaction.customerToken.equals(tokenHolder.getTokens().get(0))
                        && transaction.amount.equals(BigDecimal.valueOf(p1.amount))
                        && transaction.description.equals(p1.description));

        boolean secondProductIsPresent = managerOverview.stream()
                .anyMatch(transaction -> transaction.customerToken.equals(tokenHolder.getTokens().get(1))
                        && transaction.amount.equals(BigDecimal.valueOf(p2.amount))
                        && transaction.description.equals(p2.description));

        assertTrue(firstProductIsPresent);
        assertTrue(secondProductIsPresent);
    }

    @When("the customer requests a report of transactions")
    public void theCustomerRequestsAReportOfTransactions() {
        try {
            report = customerAdapter.getCustomerReport(customerHolder.getId());
            assertNotNull(report);
        } catch (IllegalArgumentException e) {
            this.exceptionHolder.setException(e);
        }
    }

    @Then("the customer receives a report having a transaction of {int} kr for a {string} to the merchant using the same token")
    public void theCustomerReceivesAReportHavingATransactionOfKrForAToTheMerchantUsingTheSameToken(int amount, String productDescription) {
        verifyUserReport(customerHolder, amount, productDescription, tokenHolder.getTokens().get(0));
    }

    private void verifyUserReport(UserHolder userHolder, int amount, String productDescription, UUID token) {

        // check merchant is correct
        assertEquals(userHolder.getFirstName(), report.getUser().getFirstName());
        assertEquals(userHolder.getLastName(), report.getUser().getLastName());
        assertEquals(userHolder.getCpr(), report.getUser().getCprNumber());

        // Check transactions is correct
        boolean foundCorrectTransaction = report.getPayments().stream().anyMatch(payment ->
                payment.customerToken.equals(token) &&
                        payment.amount.equals(BigDecimal.valueOf(amount)) &&
                        payment.description.equals(productDescription)
        );

        assertTrue(foundCorrectTransaction);
        assertEquals(report.getPayments().size(), 1);
    }

    @Then("the merchant does not receive a report")
    public void theMerchantDoesNotReceiveAReport() {
        assertNull(report);
    }

    @Then("the customer does not receive a report")
    public void theCustomerDoesNotReceiveAReport() {
        assertNull(report);

    }

    @When("the merchant requests a report of transactions in a time interval")
    public void theMerchantRequestsAReportOfTransactionsInATimeInterval() {
        try {
            report = merchantAdapter.getMerchantReport(merchant.getId(), LocalDateTime.of(2009,01,01,12,12), LocalDateTime.now());
            assertNotNull(report);
        } catch (IllegalArgumentException e) {
            this.exceptionHolder.setException(e);
        }
    }
}
