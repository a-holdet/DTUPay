package reportservice;

import paymentservice.Payment;

public interface IReportService {
    Report generateReportFor(String merchantId);
    void registerPayment(Payment payment);
}
