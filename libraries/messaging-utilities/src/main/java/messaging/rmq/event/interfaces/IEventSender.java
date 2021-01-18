package messaging.rmq.event.interfaces;

import messaging.rmq.event.objects.Event;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface IEventSender {
	void sendEvent(Event event);
}
