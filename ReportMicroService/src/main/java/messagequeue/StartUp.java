package messagequeue;

/***
 * @Author Jakob Vestergaard Offersen, s163940
 */

import accountservice.MessageQueueAccountService;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventSender;
import reportservice.IReportService;
import reportservice.ITransactionsRepository;
import reportservice.LocalReportService;
import reportservice.TransactionsInMemoryRepository;

public class StartUp {

    public static void main(String[] args) throws Exception {
        // Instantiate listeners
        IEventSender eventSender1 = new EventExchangeFactory().getExchange().createIEventSender();
        IEventSender eventSender2 = new EventExchangeFactory().getExchange().createIEventSender();

        ITransactionsRepository transactionsRepository = new TransactionsInMemoryRepository();

        var accountService = new MessageQueueAccountService(eventSender1);
        IReportService reportService = new LocalReportService(transactionsRepository, accountService);

        MessageQueueConnector messageQueueConnector = new MessageQueueConnector(eventSender2,reportService);

        EventPortAdapter eventPortAdapter = new EventPortAdapter();
        eventPortAdapter.registerReceiver(messageQueueConnector);
        eventPortAdapter.registerReceiver(accountService);

        new EventQueue(eventPortAdapter).startListening();
    }
}