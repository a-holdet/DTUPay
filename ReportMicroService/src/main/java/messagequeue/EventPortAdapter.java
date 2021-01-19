package messagequeue;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/***
 * @Author Michael Davidsen Kirkegaard, s153587
 */

public class EventPortAdapter implements IEventReceiver {

	private ConcurrentHashMap<String, List<IEventReceiver>> eventTypeMapper = new ConcurrentHashMap<>();

	public EventPortAdapter() { }

	public void registerReceiver(IEventReceiver service) {
		for(EventType et : service.getSupportedEventTypes()) {
			for(String etr : Arrays.asList(et.getName(), et.failed(), et.succeeded())) {
				var lst = eventTypeMapper.getOrDefault(etr, null);
				if (lst == null)
					eventTypeMapper.put(etr, Collections.synchronizedList(Arrays.asList(service)));
				else {
					lst.add(service);
				}
			}
		}
	}

	@Override
	public EventType[] getSupportedEventTypes() {
		return eventTypeMapper.keySet().stream().map(EventType::new).toArray(EventType[]::new);
	}

	@Override
	public void receiveEvent(Event event) {
		System.out.println("--------------------------------------------------------");
		System.out.println("Event received! : " + event);

		String type = event.getEventType();
		UUID eventId = event.getUUID();

		var lst = eventTypeMapper.getOrDefault(type,null);
		if(lst != null)
			for(IEventReceiver receiver : lst)
				new Thread(() -> receiver.receiveEvent(event)).start();

		System.out.println("--------------------------------------------------------");
	}
}