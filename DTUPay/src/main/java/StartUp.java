import merchantservice.MessageQueueAccountService;
import paymentservice.PaymentService;
import reportservice.ReportService;

public class StartUp {

    public static void main(String[] args) throws Exception {
        // TODO: Should prob be deleted
        MessageQueueAccountService.getInstance();
        PaymentService.getInstance();
        ReportService.getInstance();
    }
}