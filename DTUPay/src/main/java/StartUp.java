//import customerservice.CustomerPortAdapter;
//import customerservice.ICustomerService;
//import customerservice.LocalCustomerService;
//import eventservice.RabbitMQEventService;
//import merchantservice.MessageQueueMerchantService;
//import messaging.rmq.event.EventExchange;
//import messaging.rmq.event.EventQueue;
//import paymentservice.PaymentService;
//import reportservice.ReportService;
//
//public class StartUp {
//
//    public static void main(String[] args) throws Exception {
//        RabbitMQEventService.getInstance();
//        MessageQueueMerchantService.getInstance();
//        PaymentService.getInstance();
//        ReportService.getInstance();
//
//        ICustomerService service = new LocalCustomerService();
//        CustomerPortAdapter cpa = new CustomerPortAdapter(service, EventExchange.instance.getSender());
//        new EventQueue().registerReceiver(cpa);
//    }
//}