package main;

import customerservice.CustomerPortAdapter;
import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;

public class StartUp {
    public static void main(String[] args) throws Exception {
        System.out.println("called");
        ICustomerService service = new LocalCustomerService();
        CustomerPortAdapter cpa = new CustomerPortAdapter(service, EventExchange.instance.getSender());
        new EventQueue().registerReceiver(cpa);

    }
}
