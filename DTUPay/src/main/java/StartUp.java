import merchantservice.MessageQueueAccountService;
import paymentservice.MessageQueuePaymentService;
import reportservice.ReportService;

public class StartUp {

    public static void main(String[] args) throws Exception {
        MessageQueueAccountService.getInstance();
        MessageQueuePaymentService.getInstance();
        ReportService.getInstance();
    }
}