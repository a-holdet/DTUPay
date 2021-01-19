package messagequeue;

import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;

public class EventPortAdapterFactory {
    static EventPortAdapter service;
    public EventPortAdapter getPortAdapter() {
        if(service == null) {
            service = new EventPortAdapter();
            new EventQueue(service).startListening();
        }
        return service;
    }
}
