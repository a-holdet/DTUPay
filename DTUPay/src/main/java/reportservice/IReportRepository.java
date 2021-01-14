package reportservice;

import paymentservice.Payment;

import java.util.List;

public interface IReportRepository {

    void registerPayment(Payment payment);
    List<Payment> getPaymentsFor(String merchantId);
}
