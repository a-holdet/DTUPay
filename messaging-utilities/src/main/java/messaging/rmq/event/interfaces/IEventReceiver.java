package messaging.rmq.event.interfaces;

import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

public interface IEventReceiver {
	EventType[] getSupportedEventTypes();
	void receiveEvent(Event event);
}
