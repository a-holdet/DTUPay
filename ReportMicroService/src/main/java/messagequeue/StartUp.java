package messagequeue;

import accountservice.MessageQueueAccountService;
import messaging.rmq.event.EventExchangeFactory;

public class StartUp {

    public static void main(String[] args) throws Exception {
        // Instantiate listeners
        var eventSender1 = new EventExchangeFactory().getExchange().createIEventSender();
        var eventSender2 = new EventExchangeFactory().getExchange().createIEventSender();

        var reportService = new MessageQueueConnector(eventSender1);
        var accountService = new MessageQueueAccountService(eventSender2);

        var eventPortAdapter = new EventPortAdapterFactory().getPortAdapter();
        eventPortAdapter.registerReceiver(accountService);
        eventPortAdapter.registerReceiver(reportService);
    }
}