// package eventservice;

// import messaging.Event;
// import messaging.EventReceiver;
// import messaging.EventSender;

// import eventservice.Service1Factory;

// public class Service1 /* implements EventReceiver */ {

// 	public static Service1 instance = new Service1Factory().getService();

// 	EventSender sender;

// 	public Service1(EventSender sender) {
// 		this.sender = sender;
// 	}

// 	@Override
// 	public void receiveEvent(Event event) throws Exception {
// 		if (event.getEventType().equals("a")) {
// 			//System.out.println("handling event: "+event);
// 			Event e = new Event("b");
// 			sender.sendEvent(e);
// 		} else {
// 			//System.out.println("event ignored: "+event);
// 		}
// 	}
// }