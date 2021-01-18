import accounts.MessageQueueAccountService;
import paymentservice.MessageQueuePaymentService;
import reportservice.MessageQueueReportService;

public class StartUp {

    public static void main(String[] args) throws Exception {
        // TODO: Should prob be deleted
        MessageQueueAccountService.getInstance();
        MessageQueuePaymentService.getInstance();
        MessageQueueReportService.getInstance();
    }
}