package messagequeue;

import accountservice.MessageQueueAccountService;
import bank.DTUBankPort;
import bank.IBank;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventSender;
import paymentservice.IPaymentService;
import paymentservice.PaymentService;
import reportservice.MessageQueueReportService;
import tokenservice.MessageQueueTokenService;

public class StartUp {

    public static void main(String[] args) throws Exception {

        IEventSender eventSender1 = new EventExchangeFactory().getExchange().createIEventSender();
        IEventSender eventSender2 = new EventExchangeFactory().getExchange().createIEventSender();
        IEventSender eventSender3 = new EventExchangeFactory().getExchange().createIEventSender();
        IEventSender eventSender4 = new EventExchangeFactory().getExchange().createIEventSender();

        var accountService = new MessageQueueAccountService(eventSender1);
        var reportService = new MessageQueueReportService(eventSender2);
        var tokenService = new MessageQueueTokenService(eventSender3);

        IBank bank = new DTUBankPort();
        IPaymentService paymentService = new PaymentService(accountService, accountService, tokenService, bank, reportService);
        var paymentServicePortAdapter = new MessageQueueConnector(paymentService, eventSender4);

        EventPortAdapter eventPortAdapter = new EventPortAdapter();

        eventPortAdapter.registerReceiver(accountService);
        eventPortAdapter.registerReceiver(reportService);
        eventPortAdapter.registerReceiver(tokenService);
        eventPortAdapter.registerReceiver(paymentServicePortAdapter);

        new EventQueue(eventPortAdapter).startListening();
    }
}