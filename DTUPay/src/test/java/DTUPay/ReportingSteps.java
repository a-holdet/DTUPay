package DTUPay;

public class ReportingSteps {
    public ReportingSteps() {
        Given("^a transaction between a consumer and merchant at time \"([^\"]*)\"$", (String arg0) -> {
        });
        And("^another transaction between them at time \"([^\"]*)\"$", (String arg0) -> {
        });
        When("^the merchant requests a report in the interval \"([^\"]*)\" to \"([^\"]*)\"$", (String arg0, String arg1) -> {
        });
        Then("^the report only includes the transaction at time \"([^\"]*)\"$", (String arg0) -> {
        });
    }
}
