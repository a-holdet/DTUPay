import accountservice.AccountServiceFactory;
import paymentservice.PaymentServiceFactory;
import reportservice.ReportServiceFactory;
import tokenservice.TokenServiceFactory;

public class StartUp {

    public static void main(String[] args) throws Exception {
        // creates instances of listeners
        new AccountServiceFactory().getService();
        new PaymentServiceFactory().getService();
        new ReportServiceFactory().getService();
        new TokenServiceFactory().getService();
    }
}