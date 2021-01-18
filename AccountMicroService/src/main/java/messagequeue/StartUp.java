package messagequeue;

public class StartUp {

    public static void main(String[] args) throws Exception {
        // Creates new instances of listeners
        new EventPortAdapterFactory().getPortAdapter();
    }
}