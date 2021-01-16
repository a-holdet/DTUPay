package messagequeue;

import messaging.rmq.event.objects.Event;

public class StartUp {

    public static void main(String[] args) throws Exception {
        EventService.startUp();
    }
}