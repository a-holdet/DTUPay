package reportservice;

import accountservice.CustomerDoesNotExistException;
import accountservice.MerchantDoesNotExistException;

import java.time.LocalDateTime;
import java.util.List;
/***
 * @Author Benjamin Wrist Lam, s153486
 */

public interface IReportService {
    UserReport generateReportForCustomer(String customerId, LocalDateTime startTime, LocalDateTime EndTime) throws CustomerDoesNotExistException;
    UserReport generateReportForMerchant(String merchantId, LocalDateTime startTime, LocalDateTime EndTime) throws MerchantDoesNotExistException;
    void registerTransaction(Payment payment, String customerId);
    List<Transaction> generateManagerOverview();
}
