package RabbitTest;

import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

public class Service1 implements EventReceiver {

	EventSender sender;

	public Service1(EventSender sender) {
		this.sender = sender;
	}

	@Override
	public void receiveEvent(Event event) throws Exception {
		if (event.getEventType().equals("a")) {
			System.out.println("handling event: "+event);
			Event e = new Event("b");
			sender.sendEvent(e);
		} else {
			System.out.println("event ignored: "+event);
		}
	}

}
