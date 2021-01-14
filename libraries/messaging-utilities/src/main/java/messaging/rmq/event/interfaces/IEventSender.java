package messaging.rmq.event.interfaces;

import messaging.rmq.event.objects.Event;

public interface IEventSender {
	void sendEvent(Event event) throws Exception;
}
