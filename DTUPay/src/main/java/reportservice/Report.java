package reportservice;

import dtu.ws.fastmoney.User;

import java.util.List;

public class Report {

    private List<Transaction> transactions;
    private User merchant;

    public Report() {}

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public User getMerchant() {
        return merchant;
    }

    public void setMerchant(User merchant) {
        this.merchant = merchant;
    }


}
