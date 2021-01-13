package eventservice;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

public class Service2 implements EventReceiver {

	private EventSender eventSender;
	private CompletableFuture<Boolean> result;

	public Service2(EventSender eventSender) {
		this.eventSender = eventSender;
	}

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
