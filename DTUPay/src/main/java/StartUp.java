import merchantservice.MessageQueueAccountService;
import paymentservice.PaymentService;
import reportservice.ReportService;

public class StartUp {

    public static void main(String[] args) throws Exception {
        MessageQueueAccountService.getInstance();
        PaymentService.getInstance();
        ReportService.getInstance();
    }
}