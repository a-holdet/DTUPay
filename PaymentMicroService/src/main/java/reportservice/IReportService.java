package reportservice;

import paymentservice.Payment;

public interface IReportService {
    void registerTransaction(Payment payment, String customerId);
}
