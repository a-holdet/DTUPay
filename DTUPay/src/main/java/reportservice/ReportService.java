package reportservice;

import DTO.DTUPayUser;
import customerservice.Customer;
import customerservice.CustomerDoesNotExistException;
import customerservice.ICustomerService;
import merchantservice.*;
import DTO.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService implements IReportService {

    private static ReportService instance;

    public static ReportService getInstance() {
        if(instance == null) {
            instance = new ReportService(
                    new TransactionsInMemoryRepository(),
                    MessageQueueAccountService.getInstance(),
                    MessageQueueAccountService.getInstance()
            );
        }
        return instance;
    }

    private final ITransactionsRepository transactionsRepository;
    private final IMerchantService merchantService;
    private final ICustomerService customerService;

    public ReportService(ITransactionsRepository transactionsRepository, IMerchantService merchantService,
            ICustomerService customerService) {
        this.transactionsRepository = transactionsRepository;
        this.merchantService = merchantService;
        this.customerService = customerService;
        instance = this; // needed for service tests!
    }

    @Override
    public UserReport generateReportForCustomer(String customerId, String startTime, String endTime)
            throws CustomerDoesNotExistException {
        LocalDateTime startTimeAsDateTime = startTime != null ? LocalDateTime.parse(startTime) : LocalDateTime.MIN;
        LocalDateTime endTimeAsDateTime = endTime != null ? LocalDateTime.parse(endTime) : LocalDateTime.MAX;

        List<Payment> payments = transactionsRepository.getTransactionsForCustomer(customerId).stream()
                .filter(t -> t.datetime.isAfter(startTimeAsDateTime) && t.datetime.isBefore(endTimeAsDateTime))
                .map(Transaction::toPayment).collect(Collectors.toList());

        Customer customer = customerService.getCustomer(customerId);
        
        DTUPayUser customerAsUser = new DTUPayUser(customer.firstName, customer.lastName, customer.cprNumber,
                customer.accountId);
        UserReport report = new UserReport();
        report.setPayments(payments);
        report.setUser(customerAsUser);

        return report;
    }

    @Override
    public UserReport generateReportForMerchant(String merchantId, String startTime, String endTime)
            throws MerchantDoesNotExistException {
        LocalDateTime startTimeAsDateTime = startTime != null ? LocalDateTime.parse(startTime) : LocalDateTime.MIN;
        LocalDateTime endTimeAsDateTime = endTime != null ? LocalDateTime.parse(endTime) : LocalDateTime.MAX;

        List<Payment> payments = transactionsRepository.getTransactionsForMerchant(merchantId).stream()
                .filter(t -> t.datetime.isAfter(startTimeAsDateTime) && t.datetime.isBefore(endTimeAsDateTime))
                .map(Transaction::toPayment).collect(Collectors.toList());

        Merchant merchant = merchantService.getMerchant(merchantId);

        DTUPayUser merchantAsUser = new DTUPayUser(merchant.firstName, merchant.lastName, merchant.cprNumber,
                merchant.accountId);
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
