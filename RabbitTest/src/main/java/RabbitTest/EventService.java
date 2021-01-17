package RabbitTest;

import messaging.rmq.event.EventQueue;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class EventService implements IEventReceiver {

	public static EventService instance = null; // startUp(); //cannot be used until after startUp();

	public static void startUp() {
		if (instance == null) {
			try {
				var s = EventExchange.instance.getSender();
				EventService service = new EventService(s);
				new EventQueue().registerReceiver(service);
				instance = service;
			} catch (Exception e) {
				throw new Error(e);
			}
		}
	}

	IEventSender sender;

//	public EventService() {
//		try {
//			this.sender = EventExchange.instance.getSender();
//			new EventQueue().registerReceiver(this);
//		}
//		catch (Exception e) {
//			throw new Error(e);
//		}
//	}

	public EventService(IEventSender sender) {
		this.sender = sender;
	}

	public void sendEvent(Event event) throws Exception {
		System.out.println("Event Sent: " + event);
		/* handle event and stuff */
		this.sender.sendEvent(event);
	}

	@Override
	public void receiveEvent(Event event) throws Exception {
		System.out.println("handling event: " + event);
		if (event.getEventType().equals("RabbitTest a")) {
			Event e = new Event("RabbitTest b");
			this.sendEvent(e);
		} else {
			System.out.println("event ignored: " + event);
		}
	}
}