package CustomerMobileApp;

import CustomerMobileApp.DTO.Payment;
import dtu.ws.fastmoney.User;

import java.util.List;

public class Report {

    private List<Payment> payments;
    private User merchant;

    public Report() {}

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public User getMerchant() {
        return merchant;
    }

    public void setMerchant(User merchant) {
        this.merchant = merchant;
    }

}
