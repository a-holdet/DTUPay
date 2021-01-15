//package RabbitTest;
//
//import messaging.rmq.event.interfaces.IEventReceiver;
//import messaging.rmq.event.objects.Event;
//
//public class EventQueue implements IEventReceiver {
//
//    @Override
//    public void receiveEvent(Event event) throws Exception {
//
//        if (event.getEventType().equals("a")) {
//            System.out.println("handling event: " + event);
//            Event e = new Event("b");
//            this.sendEvent(e);
//        } else {
//            System.out.println("event ignored: " + event);
//        }
//    }
//}
