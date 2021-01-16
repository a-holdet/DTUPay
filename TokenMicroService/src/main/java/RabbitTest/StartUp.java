package RabbitTest;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.objects.Event;

public class StartUp {

    public static void main(String[] args) throws Exception {
        EventService.startUp();
    }
}