package reportservice;

/***
 * @Author Benjamin Wrist Lam, s153486
 */

import accountservice.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LocalReportService implements IReportService {
    private final ITransactionsRepository transactionsRepository;
    private final IAccountService accountService;

    public LocalReportService(ITransactionsRepository transactionsRepository, IAccountService accountService) {
        this.transactionsRepository = transactionsRepository;
        this.accountService = accountService;
    }

    @Override
    public UserReport generateReportForCustomer(String customerId, LocalDateTime startTime, LocalDateTime endTime) throws CustomerDoesNotExistException {
        LocalDateTime filterStartTime = getStartDateTime(startTime); //get default values to use in filter if current value is null
        LocalDateTime filterEndTime = getEndDateTime(endTime); //get default values to use in filter if current value is null

        List<Payment> payments = transactionsRepository.getTransactionsForCustomer(customerId).stream()
                .filter(t -> t.datetime.isAfter(filterStartTime) && t.datetime.isBefore(filterEndTime))
                .map(Transaction::toPayment).collect(Collectors.toList());

        Customer customer = accountService.getCustomer(customerId);

        return generateUserReport(customer, payments);
    }

    private UserReport generateUserReport(DTUPayUser user, List<Payment> payments){
        UserReport report = new UserReport();
        report.setPayments(payments);
        report.setUser(user);

        return report;
    }

    @Override
    public UserReport generateReportForMerchant(String merchantId, LocalDateTime startTime, LocalDateTime endTime) throws MerchantDoesNotExistException {
        LocalDateTime filterStartTime = getStartDateTime(startTime); //get default values to use in filter if current value is null
        LocalDateTime filterEndTime = getEndDateTime(endTime); //get default values to use in filter if current value is null

        List<Payment> payments = transactionsRepository.getTransactionsForMerchant(merchantId).stream()
                .filter(t -> t.datetime.isAfter(filterStartTime) && t.datetime.isBefore(filterEndTime))
                .map(Transaction::toPayment).collect(Collectors.toList());

        Merchant merchant = accountService.getMerchant(merchantId);

        return generateUserReport(merchant, payments);
    }
    private LocalDateTime getStartDateTime(LocalDateTime datetime){
        return datetime != null ? datetime : LocalDateTime.MIN;
    }

    private LocalDateTime getEndDateTime(LocalDateTime datetime){
        return datetime != null ? datetime : LocalDateTime.MAX;
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
