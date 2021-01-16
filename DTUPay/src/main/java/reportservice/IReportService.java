package reportservice;

import paymentservice.MerchantDoesNotExistException;
import paymentservice.Payment;

import java.util.List;

public interface IReportService {
    UserReport generateReportForCustomer(String customerId, String startTime, String EndTime) throws CustomerDoesNotExistsException;
    UserReport generateReportForMerchant(String merchantId, String startTime, String EndTime) throws MerchantDoesNotExistException;
    void registerTransaction(Payment payment, String customerId);
    List<Transaction> generateManagerOverview();
}
