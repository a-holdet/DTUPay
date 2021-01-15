package RabbitTest;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class EventService implements IEventReceiver {
	IEventSender sender;

	public EventService(IEventSender sender) {
		this.sender = sender;
	}

	@Override
	public void receiveEvent(Event event) throws Exception {
		if (event.getEventType().equals("a")) {
			System.out.println("handling event: " + event);
			Event e = new Event("b");
			sender.sendEvent(e);
		} else {
			System.out.println("event ignored: " + event);
		}
	}
}
