package messagequeue;

import customerservice.Customer;
import merchantservice.Merchant;
import customerservice.CustomerDoesNotExistException;
import customerservice.ICustomerService;
import merchantservice.IMerchantService;
import merchantservice.MerchantDoesNotExistException;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

import java.util.UUID;

public class MessageQueueConnector implements IEventReceiver {

	IMerchantService merchantService;
	ICustomerService customerService;
	IEventSender sender;

	public MessageQueueConnector(IMerchantService merchantService, ICustomerService customerService, IEventSender sender) {
		this.merchantService = merchantService;
		this.customerService = customerService;
		this.sender = sender;
	}

	private final EventType registerMerchant = new EventType("registerMerchant");
	private final EventType getMerchant = new EventType("getMerchant");
	private final EventType registerCustomer = new EventType("registerCustomer");
	private final EventType customerExists = new EventType("customerExists");
	private final EventType getCustomer = new EventType("getCustomer");

	public void registerMerchant(Merchant merchant, UUID eventID)  {
		try {
			String merchantId = merchantService.registerMerchant(merchant);
			Event event = new Event(registerMerchant.succeeded(), new Object[] { merchantId }, eventID);
			this.sender.sendEvent(event);
		} catch (IllegalArgumentException e) {
			this.sender.sendEvent(Event.GetFailedEvent(registerMerchant, e, eventID));
		}
	}

	public void registerCustomer(Customer customer, UUID eventID) {
		try {
			String customerId = customerService.registerCustomer(customer);
			Event event = new Event(registerCustomer.succeeded(), new Object[] { customerId }, eventID);
			this.sender.sendEvent(event);
		} catch (IllegalArgumentException e) {
			this.sender.sendEvent(Event.GetFailedEvent(registerCustomer, e, eventID));
		}
	}

	public void getMerchant(String merchantId, UUID eventID) {
		try {
			Merchant merchant = merchantService.getMerchant(merchantId);
			Event event = new Event(getMerchant.succeeded(), new Object[] { merchant }, eventID);
			this.sender.sendEvent(event);
		} catch (MerchantDoesNotExistException e) {
			this.sender.sendEvent(Event.GetFailedEvent(getMerchant, e, eventID));
		}
	}

	public void getCustomer(String customerId, UUID eventID) {
		try {
			Customer customer = customerService.getCustomer(customerId);
			Event event = new Event(getCustomer.succeeded(), new Object[] { customer }, eventID);
			this.sender.sendEvent(event);
		} catch (CustomerDoesNotExistException e) {
			this.sender.sendEvent(Event.GetFailedEvent(getCustomer, e, eventID));
		}
	}

	public void customerExists(String customerId, UUID eventId) {
		boolean exists = customerService.customerExists(customerId);
		this.sender.sendEvent(new Event(customerExists.succeeded(), new Object[] { exists }, eventId));
	}

	@Override
	public EventType[] getSupportedEventTypes() {
		return new EventType[] {registerMerchant, getMerchant, registerCustomer, customerExists, getCustomer};
	}

	@Override
	public void receiveEvent(Event event) {
		System.out.println("--------------------------------------------------------");
		System.out.println("Event received! : " + event);

		String type = event.getEventType();
		UUID eventId = event.getUUID();

		try {
			if (type.equals(registerMerchant.getName())) {
				Merchant merchant = event.getArgument(0, Merchant.class);
				registerMerchant(merchant, eventId);

			} else if (type.equals(getMerchant.getName())) {
				String merchantId = event.getArgument(0, String.class);
				getMerchant(merchantId, eventId);

			} else if (type.equals(getCustomer.getName())) {
				String customerId = event.getArgument(0, String.class);
				getCustomer(customerId, eventId);

			} else if (type.equals(registerCustomer.getName())) {
				Customer customer = event.getArgument(0, Customer.class);
				registerCustomer(customer, eventId);

			} else if (type.equals(customerExists.getName())) {
				String id = event.getArgument(0, String.class);
				customerExists(id, eventId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}