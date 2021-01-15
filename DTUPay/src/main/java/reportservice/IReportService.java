package reportservice;

import paymentservice.Payment;

import java.util.List;

public interface IReportService {
    Report generateReportFor(String merchantId);
    void registerPayment(Payment payment);

    List<Report> generateAllReports();
}
