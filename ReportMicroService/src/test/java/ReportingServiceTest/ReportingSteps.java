package ReportingServiceTest;

/***
 * @Author Benjamin Wrist Lam, s153486
 */
import accountservice.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import reportservice.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportingSteps {
    private static class AccountServiceMock implements IAccountService {

        private Customer customer;
        private Merchant merchant;

        @Override
        public String registerCustomer(Customer customer) throws IllegalArgumentException {
            this.customer = customer;
            String id = "Mock Customer id";
            customer.id = id;
            return id;
        }

        @Override
        public Customer getCustomer(String customerId) throws CustomerDoesNotExistException {
            return customer;
        }

        @Override
        public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
            return merchant;
        }
    }

    String merchantId = "merchantId";
    String descriptionT1 = "Old Payment";
    String descriptionT2 = "NEW Payment";
    IAccountService accountService = new AccountServiceMock();
    IReportService reportService;
    ITransactionsRepository transactionsRepository;
    String customerId;
    UserReport userReport;
    LocalDateTime start = LocalDateTime.MIN;
    LocalDateTime end = LocalDateTime.MAX;
    private List<Transaction> transactions = new ArrayList<>();

    public ReportingSteps() {
        transactionsRepository = new TransactionsInMemoryRepository();
        reportService = new LocalReportService(transactionsRepository, accountService);
    }

    @Given("a customer that is registered")
    public void aCustomerThatIsRegistered() {
        Customer customer = new Customer();
        customer.accountId = "CustomerAccountID";
        customer.firstName = "Joe";
        customer.lastName = "Exotic";
        customer.cprNumber = "121294-0014";
        customerId = customer.id = accountService.registerCustomer(customer);
    }


    @And("a transaction is made on date {string} within the time interval")
    public void aTransactionIsMadeOnDateWithinTheTimeInterval(String dateTime) {
        Transaction t1 = new Transaction(UUID.randomUUID(), merchantId,
                descriptionT1, new BigDecimal(10),
                customerId, LocalDateTime.parse(dateTime));
        transactionsRepository.registerTransaction(t1);

        transactionsRepository.registerTransaction(t1);

        transactions.add(t1);
    }

    @And("a transaction is made on date {string} outside the time interval")
    public void aTransactionIsMadeOnDateOutsideTheTimeInterval(String dateTime) {


        Transaction t2 = new Transaction(UUID.randomUUID(), merchantId,
                descriptionT2, new BigDecimal(10),
                customerId, LocalDateTime.parse(dateTime));
        transactionsRepository.registerTransaction(t2);

        transactionsRepository.registerTransaction(t2);

        transactions.add(t2);
    }

    @When("the requester requests a report in the interval between {string} and {string}")
    public void theRequesterRequestsAReportInTheIntervalBetweenAnd(String start, String end) {
        this.start = LocalDateTime.parse(start);
        this.end = LocalDateTime.parse(end);
        if(merchantId.equals("merchantId")){
            try {
                userReport = reportService.generateReportForCustomer(customerId,this.start,this.end);
            } catch (CustomerDoesNotExistException e) {
                e.printStackTrace();
            }
        }
        if(customerId.equals("customerId")){
            try {
                userReport = reportService.generateReportForMerchant(merchantId,this.start,this.end);
            } catch (MerchantDoesNotExistException e) {
                e.printStackTrace();
            }
        }
    }

    @When("the requester requests a report")
    public void theRequesterRequestsAReport() throws CustomerDoesNotExistException, MerchantDoesNotExistException {
        if(merchantId.equals("merchantId")){
            userReport = reportService.generateReportForCustomer(customerId,null,null);
        }
        if(customerId.equals("customerId")){
            userReport = reportService.generateReportForMerchant(merchantId,null,null);
        }
    }

    @Then("the customer receives only the transaction from the interval")
    public void theCustomerReceivesOnlyTheTransactionFromTheInterval() {
        List<String> descriptions = new ArrayList<>();
        userReport.getPayments().forEach(p->descriptions.add(p.description));
        assertTrue(!descriptions.contains(descriptionT2));
    }

    @Then("the customer receives all the transactions")
    public void theCustomerReceivesAllTheTransactions() {List<String> descriptions = new ArrayList<>();
        assertTrue(userReport.getPayments().stream().anyMatch(payment -> payment.description.equals(transactions.get(0).description)));
        assertTrue(userReport.getPayments().stream().anyMatch(payment -> payment.description.equals(transactions.get(1).description)));
    }
}
