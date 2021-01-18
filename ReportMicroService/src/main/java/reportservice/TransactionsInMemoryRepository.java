package reportservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionsInMemoryRepository implements ITransactionsRepository {

    private List<Transaction> transactions = new ArrayList<>();
    @Override
    public void registerTransaction(Transaction transaction) { transactions.add(transaction); }

    @Override
    public List<Transaction> getTransactionsForMerchant(String merchantId) {
        return transactions.stream().filter(t -> t.merchantId.equals(merchantId)).collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getTransactionsForCustomer(String customerId) {
        return transactions.stream().filter(t -> t.customerId.equals(customerId)).collect(Collectors.toList());
    }


    @Override
    public List<Transaction> getAllTransactions() {
        return transactions;
    }
}
