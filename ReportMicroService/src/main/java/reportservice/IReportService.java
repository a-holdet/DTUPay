package reportservice;

import AccountService.CustomerDoesNotExistException;
import AccountService.MerchantDoesNotExistException;

import java.util.List;

public interface IReportService {
    UserReport generateReportForCustomer(String customerId, String startTime, String EndTime) throws CustomerDoesNotExistException;
    UserReport generateReportForMerchant(String merchantId, String startTime, String EndTime) throws MerchantDoesNotExistException;
    void registerTransaction(Payment payment, String customerId);
    List<Transaction> generateManagerOverview();
}