package messaging.rmq.event.interfaces;

import messaging.rmq.event.objects.Event;

public interface IEventReceiver {
	void receiveEvent(Event event) throws Exception;
}
