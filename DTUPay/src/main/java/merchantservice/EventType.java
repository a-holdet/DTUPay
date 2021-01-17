package merchantservice;


public class EventType implements Resultable {

    private String name;

    public EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String succeeded() {
        return name + "Success";
    }

    @Override
    public String failed() {
        return name + "Fail";
    }

    public boolean matches(String other) {
        return other.equals(this.succeeded()) || other.equals(this.failed());
    }
}
