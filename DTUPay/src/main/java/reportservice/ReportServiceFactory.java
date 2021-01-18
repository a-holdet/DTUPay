package reportservice;

import accountservice.AccountServiceFactory;
import accountservice.ICustomerService;
import accountservice.IMerchantService;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.objects.EventServiceBase;

public class ReportServiceFactory {

    static IReportService service;
    public IReportService getService() {
        if(service == null) {
            service = new MessageQueueReportService(
                    new EventExchangeFactory().getExchange().getSender(),
                    (IMerchantService) new AccountServiceFactory().getService(),
                    (ICustomerService) new AccountServiceFactory().getService()
            );
            new EventQueue((IEventReceiver) service).startListening();
        }
        return service;
    }
}
