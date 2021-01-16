package reportservice;

import paymentservice.Payment;

import java.util.List;

public interface IReportService {
    UserReport generateReportForCustomer(String customerId);
    UserReport generateReportForMerchant(String merchantId, String startTime, String EndTime);
    void registerTransaction(Payment payment, String customerId);
    List<Transaction> generateManagerOverview();
}
