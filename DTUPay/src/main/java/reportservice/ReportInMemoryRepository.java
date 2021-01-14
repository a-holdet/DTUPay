package reportservice;

import paymentservice.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportInMemoryRepository implements IReportRepository {

    private List<Payment> payments = new ArrayList<>();
    @Override
    public void registerPayment(Payment payment) {
        payments.add(payment);
    }

    @Override
    public List<Payment> getPaymentsFor(String merchantId) {
        return payments.stream().filter(p -> p.merchantId.equals(merchantId)).collect(Collectors.toList());
    }
}
