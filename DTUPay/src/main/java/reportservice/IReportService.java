package reportservice;

import DTO.Transaction;
import DTO.UserReport;
import accountservice.CustomerDoesNotExistException;
import accountservice.MerchantDoesNotExistException;

import java.util.List;

public interface IReportService {
    UserReport generateReportForCustomer(String customerId, String startTime, String EndTime) throws CustomerDoesNotExistException;
    UserReport generateReportForMerchant(String merchantId, String startTime, String EndTime) throws MerchantDoesNotExistException;
    List<Transaction> generateManagerOverview();
}
