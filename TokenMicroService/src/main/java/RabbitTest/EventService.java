package RabbitTest;

import messaging.rmq.RMQQueue;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class EventService extends EventQueue implements IEventReceiver {
	IEventSender sender;

	public EventService() {
		super();
		this.sender = super.parentExchange.getSender();
	}

	public EventService(IEventSender sender) {
		super();
		this.sender = sender;
	}

	@Override
	public void receiveEvent(Event event) throws Exception {

		if (event.getEventType().equals("a")) {
			System.out.println("handling event: " + event);
			Event e = new Event("b");
			this.sendEvent(e);
		} else {
			System.out.println("event ignored: " + event);
		}
	}

	public void sendEvent(Event event) throws Exception {
		/* handle event and stuff */
		this.sender.sendEvent(event);
	}
}