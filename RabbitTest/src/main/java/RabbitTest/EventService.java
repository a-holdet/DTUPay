package RabbitTest;

import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

public class EventService implements EventReceiver {

	EventSender sender;

	public EventService(EventSender sender) {
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
