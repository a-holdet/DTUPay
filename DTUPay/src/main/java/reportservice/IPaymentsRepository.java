package reportservice;

import paymentservice.Payment;

import java.util.List;

public interface IPaymentsRepository {

    void registerPayment(Payment payment);
    List<Payment> getPaymentsFor(String merchantId);
    List<Payment> getAllPayments();
}
