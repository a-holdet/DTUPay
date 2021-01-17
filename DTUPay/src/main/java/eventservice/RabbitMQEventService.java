package eventservice;

import java.util.concurrent.CompletableFuture;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class RabbitMQEventService implements IEventService, IEventReceiver {

	// Singleton as method due to serviceTest
	private static RabbitMQEventService instance;
	public static RabbitMQEventService getInstance() {
		if (instance == null) {
			try {
				var ies = EventExchange.instance.getSender();
				RabbitMQEventService service = new RabbitMQEventService(ies);
				new EventQueue().registerReceiver(service);
				instance = service;
			} catch (Exception e) {
				throw new Error(e);
			}
		}
		return instance;
	}

	private IEventSender eventSender;
	private CompletableFuture<Boolean> result;

	public RabbitMQEventService(IEventSender eventSender) {
		this.eventSender = eventSender;
	}

	@Override
	public boolean doSomething() throws Exception {
		Event event = new Event("RabbitTest a");
		result = new CompletableFuture<>(); // I promise that in the future i will have a value. (Asyc communication)

		eventSender.sendEvent(event);

		return result.join(); // Get the future value of result.
	}

	@Override
	public void receiveEvent(Event event) throws Exception {
		if (event.getEventType().equals("RabbitTest b")) {
			System.out.println("event handled: " + event);

			result.complete(true);	// set the future value to true which completes the future.

			event = new Event("TokenTest a");
			eventSender.sendEvent(new Event("TokenTest a"));
		} else {
			System.out.println("event ignored: " + event);
		}
	}

}
