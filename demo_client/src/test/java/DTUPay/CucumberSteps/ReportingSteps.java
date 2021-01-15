package DTUPay.CucumberSteps;

import CustomerMobileApp.*;
import CustomerMobileApp.DTO.Report;
import DTUPay.Holders.MerchantHolder;
import DTUPay.Holders.OtherMerchantHolder;
import DTUPay.Holders.PurchasesHolder;
import DTUPay.Holders.TokenHolder;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ReportingSteps {

    //Adapters
    MerchantAdapter merchantAdapter = new MerchantAdapter();
    DTUManagerAdapter managerAdapter = new DTUManagerAdapter();
    // Holders
    private MerchantHolder merchant;
    private OtherMerchantHolder otherMerchant;
    private TokenHolder tokenHolder;
    private PurchasesHolder purchasesHolder;
    //Class specifics
    private Report report;
    private List<Report> managerReports;


    public ReportingSteps(MerchantHolder merchant, OtherMerchantHolder otherMerchant, TokenHolder tokenHolder, PurchasesHolder purchasesHolder) {
        this.merchant = merchant;
        this.otherMerchant = otherMerchant;
        this.tokenHolder = tokenHolder;
        this.purchasesHolder = purchasesHolder;
    }

    @When("the merchant requests a report of transactions")
    public void theMerchantRequestsAReportOfTransactions() {
        report = merchantAdapter.getMerchantReport(merchant.getId());
        assertNotNull(report);
    }

    @Then("the merchant receives a report having a transaction of {int} kr for a {string} to the merchant using the same token")
    public void theMerchantReceivesAReportHavingATransactionOfKrForAToTheMerchantUsingTheSameToken(int amount, String productDescription) {
        // check merchant is correct
        assertEquals(merchant.getFirstName(), report.getUser().getFirstName());
        assertEquals(merchant.getLastName(), report.getUser().getLastName());
        assertEquals(merchant.getCpr(), report.getUser().getCprNumber());

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

    @When("DTUPay requests a report of transactions")
    public void dtupayRequestsAReportOfTransactions() {
        managerReports = managerAdapter.getReport();
        assertNotNull(managerReports);
    }

    @Then("DTUPay receives a report including both transactions")
    public void dtupayReceivesAReportIncludingBothTransactions() {
        PurchasesHolder.Purchase p1 = purchasesHolder.getPurchases().get(0);
        PurchasesHolder.Purchase p2 = purchasesHolder.getPurchases().get(1);

        // Assert first payment is included
        System.out.println("TOKENS:");
        System.out.println(tokenHolder.getTokens().get(0));
        System.out.println(tokenHolder.getTokens().get(1));
        System.out.println(tokenHolder.getTokens().size());
        System.out.println("XXX");
        System.out.println(managerReports.get(0).getPayments().size());
        System.out.println(managerReports.get(0).getPayments().get(0).description);
        System.out.println(managerReports.get(0).getPayments().get(0).customerToken);
        System.out.println("---");
        System.out.println(managerReports.get(1).getPayments().size());
        System.out.println(managerReports.get(1).getPayments().get(0).description);
        System.out.println(managerReports.get(1).getPayments().get(0).customerToken);

        System.out.println("...");
        System.out.println(managerReports.size());

        Report firstReport = managerReports.stream().filter(r -> r.getUser().getCprNumber().equals(merchant.getCpr())).collect(Collectors.toList()).get(0);
        Report secondReport = managerReports.stream().filter(r -> r.getUser().getCprNumber().equals(otherMerchant.getCpr())).collect(Collectors.toList()).get(0);

        boolean firstProductIsPresent = firstReport.getPayments().stream().anyMatch(payment ->
                payment.customerToken.equals(tokenHolder.getTokens().get(0)) &&
                payment.amount.equals(BigDecimal.valueOf(p1.amount)) &&
                payment.description.equals(p1.description)
        );

        // Assert second payment is included
        boolean secondProductIsPresent = secondReport.getPayments().stream().anyMatch(payment ->
                payment.customerToken.equals(tokenHolder.getTokens().get(1)) &&
                payment.amount.equals(BigDecimal.valueOf(p2.amount)) &&
                payment.description.equals(p2.description)
        );

        assertTrue(firstProductIsPresent);
        assertTrue(secondProductIsPresent);
    }
}
