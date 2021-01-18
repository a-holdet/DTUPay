package ReportingServiceTest.local;

import reportservice.Transaction;

import java.util.List;

public interface ITransactionsRepository {
    void registerTransaction(Transaction transaction);
    List<Transaction> getTransactionsForMerchant(String merchantId);
    List<Transaction> getTransactionsForCustomer(String customerId);
    List<Transaction> getAllTransactions();
}
