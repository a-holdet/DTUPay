package services.reportservice;

import messagequeue.EventPortAdapterFactory;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.interfaces.IEventReceiver;

/***
 * @Author Martin Hemmingsen, s141887
 */
public class ReportServiceFactory {

    static IReportService service;
    public IReportService getService() {
        if(service == null) {
            // Specific implementation of service
            service = new MessageQueueReportService(
                    new EventExchangeFactory().getExchange().createIEventSender()
            );
            // Registers to the EventPortAdapter
            new EventPortAdapterFactory().getPortAdapter()
                    .registerReceiver( (IEventReceiver) service);
        }
        return service;
    }
}
