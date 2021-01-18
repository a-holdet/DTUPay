/*
package customerservice;

import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class CustomerPortAdapter implements IEventReceiver {

    private final ICustomerService customerService;
    private final IEventSender sender;

    public CustomerPortAdapter(ICustomerService customerService, IEventSender sender) {
        this.customerService = customerService;
        this.sender = sender;
    }

    @Override
    public void receiveEvent(Event event) {
        System.out.println("CustomerPortAdapter event received " + event);
        if (event.getEventType().equals("customerExists")) {
            System.out.println("inside receive event cust adapter");
            boolean customerExists = customerExists(event);
            System.out.println("receive customer exists " + customerExists);
            sender.sendEvent(new Event("customerExistsResponse", new Object[]{customerExists}));
        } else if (event.getEventType().equals("registerCustomer")) {
            System.out.println("did match with (registercustomer)");
            registerCustomer(event);
        } else {
            System.out.println("ignored customerportadapter " + event);
        }
    }

    private void registerCustomer(Event event) {
        Customer customer = event.getArgument(0, Customer.class);
        String customerId = customerService.registerCustomer(customer);
        Event registerCustomerEvent = new Event("registerCustomerResponse", new Object[]{customerId});
        System.out.println("registerCustomerEventResponse " + registerCustomerEvent);
        sender.sendEvent(registerCustomerEvent);
    }

    private boolean customerExists(Event event) {
        // TODO: Error handling that checks if cust id has been sent in event
        String customerId = event.getArgument(0, String.class);

        System.out.println("Cust id customerportadapter" + customerId);

        System.out.println("customerService " + customerService);

        boolean customerExists = customerService.customerExists(customerId);
        System.out.println("cusotmerExists " + customerExists);
        return customerExists;
    }


}
*/
