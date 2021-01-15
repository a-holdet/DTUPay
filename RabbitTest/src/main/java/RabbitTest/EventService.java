package RabbitTest;

import messaging.rmq.RMQQueue;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class EventService implements IEventReceiver {

	public static EventService instance = new EventService();

	IEventSender sender;

	public EventService() {
		super();
		System.out.println("Without IEventSender");
		//this.sender = EventExchange.instance.getSender();
	}

	public EventService(IEventSender sender) {
		super();
		System.out.println("With IEventSender");
		this.sender = sender;
	}

	public void sendEvent(Event event) throws Exception {
		System.out.println("Event Sent: " + event);
		/* handle event and stuff */
		this.sender.sendEvent(event);
	}

	@Override
	public void receiveEvent(Event event) throws Exception {
//		System.out.println("handling event: " + event);
		if (event.getEventType().equals("a")) {
			Event e = new Event("b");
			this.sendEvent(e);
		} else {
			System.out.println("event ignored: " + event);
		}
	}
}

/*
* package RabbitTest;

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

* */