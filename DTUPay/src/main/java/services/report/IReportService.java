package services.report;

import DTO.Transaction;
import DTO.UserReport;

import java.util.List;

public interface IReportService {
    UserReport generateReportForCustomer(String customerId, String startTime, String EndTime) throws UserDoesNotExistsException;
    UserReport generateReportForMerchant(String merchantId, String startTime, String EndTime) throws UserDoesNotExistsException;
    List<Transaction> generateManagerOverview();
}
