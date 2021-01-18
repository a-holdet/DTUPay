package Reports;

import DTO.Payment;
import DTO.Transaction;
import DTO.UserReport;

import java.util.List;

public interface IReportService {
    void registerTransaction(Payment payment, String customerId);
}
