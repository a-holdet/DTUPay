package merchantservice;

public class EventType  {

    private String name;

    public EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    
    public String succeeded() {
        return name + "Success";
    }
    
    public String failed() {
        return name + "Fail";
    }

    public boolean matches(String other) {
        return other.equals(this.succeeded()) || other.equals(this.failed());
    }
}
