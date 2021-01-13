package eventservice;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;
import messaging.rabbitmq.RabbitMqListener;
import messaging.rabbitmq.RabbitMqSender;

public class RabbitMQEventService implements IEventService, EventReceiver {

	private static RabbitMQEventService setUpInstance() {
		EventSender b = new RabbitMqSender();
		var service2 = new RabbitMQEventService(b);
		RabbitMqListener r = new RabbitMqListener(service2);
		try {
			r.listen();
		} catch (Exception e) {
			throw new Error(e);
		}
		return service2;
	}

	public static RabbitMQEventService instance = setUpInstance();

	private EventSender eventSender;
	private CompletableFuture<Boolean> result;

	public RabbitMQEventService(EventSender eventSender) {
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
			System.out.println("event handled: "+event);
			result.complete(true);
		} else {
			System.out.println("event ignored: "+event);
		}
	}

}
