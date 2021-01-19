package messagequeue;

import messaging.rmq.event.EventQueue;

public class EventPortAdapterFactory {
    static EventPortAdapter service;
    public EventPortAdapter getPortAdapter() {
        if(service == null) {
            // instance of the port adapter
            service = new EventPortAdapter();
            // creates new queue and begins listening
            new EventQueue(service).startListening();
        }
        return service;
    }
}
