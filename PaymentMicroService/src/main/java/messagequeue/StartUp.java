package messagequeue;

public class StartUp {

    public static void main(String[] args) throws Exception {
        //instantiates listeners
        var paymentServicePortAdapter = PaymentServicePortAdaper.getInstance();
        var accountService= MessageQueueAccountService.getInstance();
        var reportService = MessageQueueReportService.getInstance();
        var tokenService = MessageQueueTokenService.getInstance();

        var eventPortAdapter = new EventPortAdapterFactory().getPortAdapter();
        eventPortAdapter.registerReceiver(paymentServicePortAdapter);
        eventPortAdapter.registerReceiver(accountService);
        eventPortAdapter.registerReceiver(reportService);
        eventPortAdapter.registerReceiver(tokenService);
    }
}