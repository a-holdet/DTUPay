package reportservice;

import DTO.DTUPayUser;
import customerservice.Customer;
import customerservice.CustomerDoesNotExcistException;
import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import merchantservice.MerchantDoesNotExistException;
import DTO.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService implements IReportService {

    public static ReportService instance = new ReportService();

    private final ITransactionsRepository transactionsRepository;
    private final IMerchantService merchantService;
    private final ICustomerService customerService;

    private ReportService() {
        this(new TransactionsInMemoryRepository(), LocalMerchantService.instance, LocalCustomerService.instance);
    }

    public ReportService(ITransactionsRepository transactionsRepository, IMerchantService merchantService, ICustomerService customerService) {
        this.transactionsRepository = transactionsRepository;
        this.merchantService = merchantService;
        this.customerService = customerService;
    }


    @Override
    public UserReport generateReportForCustomer(String customerId, String startTime, String endTime) throws CustomerDoesNotExcistException {
        LocalDateTime startTimeAsDateTime = startTime != null ? LocalDateTime.parse(startTime) : LocalDateTime.MIN;
        LocalDateTime endTimeAsDateTime = endTime != null ? LocalDateTime.parse(endTime) : LocalDateTime.MAX;

        List<Payment> payments = transactionsRepository.getTransactionsForCustomer(customerId).stream()
                .filter(t -> t.datetime.isAfter(startTimeAsDateTime) && t.datetime.isBefore(endTimeAsDateTime))
                .map(Transaction::toPayment)
                .collect(Collectors.toList());
        //TODO Refactoer methodname
        Customer customer = customerService.getCustomerWith(customerId);
        if(customer == null) throw new CustomerDoesNotExistsException("The customer does not exists in DTUPay");
        DTUPayUser customerAsUser = new DTUPayUser(customer.firstName, customer.lastName, customer.cprNumber, customer.accountId);
        UserReport report = new UserReport();
        report.setPayments(payments);
        report.setUser(customerAsUser);

        return report;
    }

    @Override
    public UserReport generateReportForMerchant(String merchantId, String startTime, String endTime) throws MerchantDoesNotExistException  {
        LocalDateTime startTimeAsDateTime = startTime != null ? LocalDateTime.parse(startTime) : LocalDateTime.MIN;
        LocalDateTime endTimeAsDateTime = endTime != null ? LocalDateTime.parse(endTime) : LocalDateTime.MAX;

        List<Payment> payments =
                transactionsRepository.getTransactionsForMerchant(merchantId).stream()
                        .filter(t -> t.datetime.isAfter(startTimeAsDateTime) && t.datetime.isBefore(endTimeAsDateTime))
                        .map(Transaction::toPayment)
                        .collect(Collectors.toList());

        Merchant merchant = merchantService.getMerchant(merchantId);
        if(merchant == null) throw new MerchantDoesNotExistException("The merchant does not exists in DTUPay");

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
