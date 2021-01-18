import merchantservice.MessageQueueAccountService;
import paymentservice.MessageQueuePaymentService;
import reportservice.ReportService;

public class StartUp {

    public static void main(String[] args) throws Exception {
        // TODO: Should prob be deleted
        MessageQueueAccountService.getInstance();
        MessageQueuePaymentService.getInstance();
        ReportService.getInstance();
    }
}