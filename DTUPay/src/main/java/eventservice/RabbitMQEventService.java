package eventservice;

import java.util.concurrent.CompletableFuture;

import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class RabbitMQEventService implements IEventService, IEventReceiver {

	static EventExchange exchange = EventExchange.instance;
	static EventQueue queue = EventQueue.instance;

	private static RabbitMQEventService setUpInstance() {
		IEventSender ies = exchange.getSender();
		var service2 = new RabbitMQEventService(ies);
		try {
			queue.registerReceiver(service2);
		} catch (Exception e) {
			throw new Error(e);
		}
		return service2;
	}

	public static RabbitMQEventService instance = setUpInstance();

	private IEventSender eventSender;
	private CompletableFuture<Boolean> result;

	public RabbitMQEventService(IEventSender eventSender) {
		this.eventSender = eventSender;
	}

	@Override
	public boolean doSomething() throws Exception {
		Event event = new Event("a");
		result = new CompletableFuture<>();

		eventSender.sendEvent(event);

		return result.join();
	}

	@Override
	public void receiveEvent(Event event) {
		if (event.getEventType().equals("b")) {
			System.out.println("event handled: " + event);
			result.complete(true);
		} else {
			System.out.println("event ignored: " + event);
		}
	}

}
