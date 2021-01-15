package CustomerMobileApp.DTO;

import java.util.List;

public class Report {

    private List<Payment> payments;
    private DTUPayUser user;

    public Report() {}

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public DTUPayUser getUser() {
        return user;
    }

    public void setUser(DTUPayUser user) {
        this.user = user;
    }
}
