package messagequeue;

import customerservice.*;
import merchantservice.*;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventReceiver;

public class StartUp {

    public static void main(String[] args) {
        IMerchantRepository merchantRepository = new MerchantInMemoryRepository();
        ICustomerRepository customerRepository = new CustomerInMemoryRepository();

        IMerchantService merchantService = new LocalMerchantService(merchantRepository);
        ICustomerService customerService = new LocalCustomerService(customerRepository);

        IEventReceiver service = new AccountServicePortAdapter(
                merchantService,
                customerService,
                new EventExchangeFactory().getExchange().createIEventSender()
        );

        new EventQueue(service).startListening();
    }
}