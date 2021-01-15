package reportservice;

import DTO.DTUPayUser;
import customerservice.Customer;
import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import paymentservice.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService implements IReportService {

    public static ReportService instance = new ReportService();

    private ITransactionsRepository transactionsRepository = new TransactionsInMemoryRepository();
    private IMerchantService merchantService = LocalMerchantService.instance;
    private ICustomerService customerService = LocalCustomerService.instance;

    private ReportService() {}


    @Override
    public UserReport generateReportForCustomer(String customerId) {
        List<Payment> payments = transactionsRepository.getTransactionsForCustomer(customerId).stream().map(Transaction::toPayment).collect(Collectors.toList());
        Customer customer = customerService.getCustomerWith(customerId);
        DTUPayUser merchantAsUser = new DTUPayUser(customer.firstName, customer.lastName, customer.cprNumber, customer.accountId);
        UserReport report = new UserReport();
        report.setPayments(payments);
        report.setUser(merchantAsUser);

        return report;
    }

    @Override
    public UserReport generateReportForMerchant(String merchantId) {
        List<Payment> payments = transactionsRepository.getTransactionsForMerchant(merchantId).stream().map(Transaction::toPayment).collect(Collectors.toList());
        Merchant merchant = merchantService.getMerchantWith(merchantId);
        DTUPayUser merchantAsUser = new DTUPayUser(merchant.firstName, merchant.lastName, merchant.cprNumber, merchant.accountId);
        UserReport report = new UserReport();
        report.setPayments(payments);
        report.setUser(merchantAsUser);

        return report;
    }

    @Override
    public void registerTransaction(Payment payment, String customerId) {
        Transaction transaction = new Transaction(payment, customerId, LocalDateTime.now());
        transactionsRepository.registerTransaction(transaction);
    }

    @Override
    public List<Transaction> generateManagerOverview() {
        return transactionsRepository.getAllTransactions();
    }
}
