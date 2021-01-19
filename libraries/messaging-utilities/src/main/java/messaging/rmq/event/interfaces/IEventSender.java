package messaging.rmq.event.interfaces;

import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface IEventSender {
	void sendEvent(Event event);
	void sendErrorEvent(EventType eventType, Exception exception, UUID eventID);
}
