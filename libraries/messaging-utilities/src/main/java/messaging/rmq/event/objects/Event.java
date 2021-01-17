package messaging.rmq.event.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class Event {

	private String eventType;
	private Object[] arguments;
	private UUID uuid;

	public Event(String eventType, Object[] arguments, UUID uuid) {
		this.eventType = eventType;
		this.arguments = arguments;
		this.uuid = uuid;
	}

	public Event(String eventType, Object[] arguments) {
		this(eventType, arguments, UUID.randomUUID());
	}

	public Event(String eventType) {
		this(eventType, new Object[]{});
	}

	public Event() {}

	public String getEventType() {
		return eventType;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public <T> T getArgument(Integer idx, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(gson.toJson(arguments[idx]), clazz);
	}

	public UUID getUUID() {
		return uuid;
	}

	public boolean isSuccessReponse() {
		return eventType.endsWith("Success");
	}

	public boolean isFailureReponse() {
		return eventType.endsWith("Fail");
	}

	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass())) {
			return false;
		}
		Event other = (Event) o;
		return this.eventType.equals(other.eventType)
				&& (this.getArguments() != null && this.getArguments().equals(other.getArguments()))
				|| (this.getArguments() == null && other.getArguments() == null);
	}

	public int hashCode() {
		return eventType.hashCode();
	}

	public String toString() {
		List<String> strs = new ArrayList<>();
		if (arguments != null) {
			List<Object> objs = Arrays.asList(arguments);
			strs = objs.stream().map(o -> o == null ? "<N/A>" : o.toString()).collect(Collectors.toList());
		}

		return String.format("event(%s,%s,%s)", eventType, String.join(",", strs), uuid == null ? "<N/A>" : uuid.toString());
	}
}
