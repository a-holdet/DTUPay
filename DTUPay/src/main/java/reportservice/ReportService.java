package reportservice;

import DTO.DTUPayUser;
import customerservice.Customer;
import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import paymentservice.Payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService implements IReportService {

    public static ReportService instance = new ReportService();

    private ITransactionsRepository transactionsRepository ;
    private IMerchantService merchantService;
    private ICustomerService customerService;

    private ReportService() {
        this(new TransactionsInMemoryRepository(), LocalMerchantService.instance, LocalCustomerService.instance);
    }

    public ReportService(ITransactionsRepository transactionsRepository, IMerchantService merchantService, ICustomerService customerService) {
        this.transactionsRepository = transactionsRepository;
        this.merchantService = merchantService;
        this.customerService = customerService;
    }


    @Override
    public UserReport generateReportForCustomer(String customerId, String startTime, String endTime) {
        LocalDateTime startTimeAsDateTime = startTime != null ? LocalDateTime.parse(startTime) : LocalDateTime.MIN;
        LocalDateTime endTimeAsDateTime = endTime != null ? LocalDateTime.parse(endTime) : LocalDateTime.MAX;

        List<Payment> payments = transactionsRepository.getTransactionsForCustomer(customerId).stream()
                .filter(t -> t.datetime.isAfter(startTimeAsDateTime) && t.datetime.isBefore(endTimeAsDateTime))
                .map(Transaction::toPayment)
                .collect(Collectors.toList());
        Customer customer = customerService.getCustomerWith(customerId);
        DTUPayUser customerAsUser = new DTUPayUser(customer.firstName, customer.lastName, customer.cprNumber, customer.accountId);
        UserReport report = new UserReport();
        report.setPayments(payments);
        report.setUser(customerAsUser);

        return report;
    }

    @Override
    public UserReport generateReportForMerchant(String merchantId, String startTime, String endTime) {
        LocalDateTime startTimeAsDateTime = startTime != null ? LocalDateTime.parse(startTime) : LocalDateTime.MIN;
        LocalDateTime endTimeAsDateTime = endTime != null ? LocalDateTime.parse(endTime) : LocalDateTime.MAX;

        List<Payment> payments =
                transactionsRepository.getTransactionsForMerchant(merchantId).stream()
                        .filter(t -> t.datetime.isAfter(startTimeAsDateTime) && t.datetime.isBefore(endTimeAsDateTime))
                        .map(Transaction::toPayment)
                        .collect(Collectors.toList());

        Merchant merchant = merchantService.getMerchant(merchantId);
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
