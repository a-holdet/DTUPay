package paymentservice;

import DTO.Transaction;
import DTO.UserReport;

import java.util.List;

public class ReportServiceMock implements IReportService {
    @Override
    public UserReport generateReportForCustomer(String customerId, String startTime, String EndTime) throws CustomerDoesNotExistException {
        return null;
    }

    @Override
    public UserReport generateReportForMerchant(String merchantId, String startTime, String EndTime) throws MerchantDoesNotExistException {
        return null;
    }

    @Override
    public void registerTransaction(Payment payment, String customerId) {

    }

    @Override
    public List<Transaction> generateManagerOverview() {
        return null;
    }
}
