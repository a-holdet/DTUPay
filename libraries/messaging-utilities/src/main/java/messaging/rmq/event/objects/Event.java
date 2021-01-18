package messaging.rmq.event.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Event {

    static Gson GSON = new Gson();

    private String eventType;
    private String[] arguments;
    private UUID uuid;

    public Event(String eventType, Object[] arguments, UUID uuid) {
        this.eventType = eventType;
        this.arguments = Arrays.stream(arguments).map(GSON::toJson).toArray(String[]::new);
        this.uuid = uuid;
    }

    public Event(String eventType, Object[] arguments) {
        this(eventType, arguments, UUID.randomUUID());
    }

    public Event(String eventType) {
        this(eventType, new Object[]{});
    }

    public Event() {
    }

    public String getEventType() {
        return eventType;
    }

    public <T> T getArgument(Integer idx, Class<T> clazz) {
        return GSON.fromJson(arguments[idx], clazz);
    }

    public <T> T getArgument(Integer idx, TypeToken<T> clazz) {
        return GSON.fromJson(arguments[idx], clazz.getType());
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isSuccessReponse() {
        return eventType.endsWith("Success");
    }

    public <T> T getPayloadAs(Class<T> payloadType) {
        return getArgument(0, payloadType);
    }

    public <T> T getPayloadAs(TypeToken<T> clazz) {
        return getArgument(0, clazz);
    }

    //Errormessage are transferred with error type at index 0 and error message at index 1
    public String getErrorMessage() {
        return getArgument(1, String.class);
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
                && (this.arguments != null && this.arguments.equals(other.arguments))
                || (this.arguments == null && other.arguments == null);
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
