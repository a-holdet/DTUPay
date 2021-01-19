package messagequeue;

import accountservice.MessageQueueAccountService;
import messaging.rmq.event.interfaces.IEventReceiver;

public class StartUp {

    public static void main(String[] args) throws Exception {
        // Instantiate listeners
        var accountService = MessageQueueAccountService.getInstance();
        var reportService = ReportServicePortAdapter.getInstance();

        var eventPortAdapter = new EventPortAdapterFactory().getPortAdapter();
        eventPortAdapter.registerReceiver(accountService);
        eventPortAdapter.registerReceiver(reportService);
    }
}