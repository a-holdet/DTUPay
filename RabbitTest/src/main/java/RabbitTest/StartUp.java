package RabbitTest;

import messaging.rmq.event.EventQueue;

public class StartUp {

	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		//EventService service = new EventService();
	}
}

/*
package RabbitTest;

import messaging.rmq.event.EventQueueEvents;
import messaging.rmq.event.interfaces.IEventSender;

public class StartUp {

	static EventQueueEvents eventQueueEvents = EventQueueEvents.instance;

	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		IEventSender s = eventQueueEvents.getSender();
		EventService service = new EventService(s);
		eventQueueEvents.registerReceiver(service);
	}
}

* */