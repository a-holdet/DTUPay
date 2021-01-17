import eventservice.RabbitMQEventService;
import merchantservice.MessageQueueMerchantService;
import paymentservice.PaymentService;
import reportservice.ReportService;

public class StartUp {

    public static void main(String[] args) throws Exception {
        RabbitMQEventService.getInstance();
        MessageQueueMerchantService.getInstance();
        PaymentService.getInstance();
        ReportService.getInstance();
    }
}