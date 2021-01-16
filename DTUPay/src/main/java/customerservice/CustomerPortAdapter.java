package customerservice;

import io.cucumber.java.an.E;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class CustomerPortAdapter implements IEventReceiver {
    public static CustomerPortAdapter instance = null; // startUp(); //cannot be used until after startUp();
    private ICustomerService customerService = LocalCustomerService.instance;

    public CustomerPortAdapter(IEventSender sender) {
        this.sender = sender;
    }

    public static void startUp() {
        if (instance == null) {
            try {
                var s = EventExchange.instance.getSender();
                CustomerPortAdapter portAdapter = new CustomerPortAdapter(s);
                new EventQueue().registerReceiver(portAdapter);
                instance = portAdapter;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    IEventSender sender;

    @Override
    public void receiveEvent(Event event) throws Exception {
        if (event.getEventType().equals("customerExists")) {
            System.out.println("inside receive event cust adapter");
            boolean customerExists = customerExists(event);
            System.out.println("receive customer exists " + customerExists);
            sender.sendEvent(new Event("customerExistsResponse", new Object[]{customerExists}));
        }
    }

    private boolean customerExists(Event event) {
        // TODO: Error handling that checks if cust id has been sent in event
        String customerId = (String) event.getArguments()[0];

        System.out.println("Cust id customerportadapter" + customerId);

        System.out.println("customerService " + customerService);

        boolean customerExists = customerService.customerExists(customerId);
        System.out.println("cusotmerExists " + customerExists);
        return customerExists;
    }


}
