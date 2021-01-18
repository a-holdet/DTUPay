package messagequeue;

import customerservice.*;
import merchantservice.*;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

import java.util.UUID;

public class EventPortAdapter implements IEventReceiver {

	IMerchantService merchantService;
	ICustomerService customerService;
	IEventSender sender;

	public EventPortAdapter(IMerchantService merchantService, ICustomerService customerService, IEventSender sender) {
		this.merchantService = merchantService;
		this.customerService = customerService;
		this.sender = sender;
	}

	private static final EventType registerMerchant = new EventType("registerMerchant");
	private static final EventType getMerchant = new EventType("getMerchant");
	private static final EventType registerCustomer = new EventType("registerCustomer");
	private static final EventType customerExists = new EventType("customerExists");
	private static final EventType getCustomer = new EventType("getCustomer");
	private static final EventType[] supportedEventTypes = { registerMerchant, getMerchant, registerCustomer,
			customerExists, getCustomer };

	public void registerMerchant(Merchant merchant, UUID eventID)  {
		try {
			String merchantId = merchantService.registerMerchant(merchant);
			Event event = new Event(registerMerchant.succeeded(), new Object[] { merchantId }, eventID);
			this.sender.sendEvent(event);
		} catch (IllegalArgumentException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(registerMerchant.failed(), new Object[] { exceptionType, exceptionMsg }, eventID);
			this.sender.sendEvent(event);
		}
	}

	public void registerCustomer(Customer customer, UUID eventID) {
		try {
			String customerId = customerService.registerCustomer(customer);
			Event event = new Event(registerCustomer.succeeded(), new Object[] { customerId }, eventID);
			this.sender.sendEvent(event);
		} catch (IllegalArgumentException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(registerCustomer.failed(), new Object[] { exceptionType, exceptionMsg }, eventID);
			this.sender.sendEvent(event);
		}
	}

	public void getMerchant(String merchantId, UUID eventID) {
		try {
			Merchant merchant = merchantService.getMerchant(merchantId);
			Event event = new Event(getMerchant.succeeded(), new Object[] { merchant }, eventID);
			this.sender.sendEvent(event);
		} catch (MerchantDoesNotExistException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(getMerchant.failed(), new Object[] { exceptionType, exceptionMsg }, eventID);
			this.sender.sendEvent(event);
		}
	}

	public void getCustomer(String customerId, UUID eventID) {
		try {
			Customer customer = customerService.getCustomer(customerId);
			Event event = new Event(getCustomer.succeeded(), new Object[] { customer }, eventID);
			this.sender.sendEvent(event);
		} catch (CustomerDoesNotExistException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(getCustomer.failed(), new Object[] { exceptionType, exceptionMsg }, eventID);
			this.sender.sendEvent(event);
		}
	}

	public void customerExists(String customerId, UUID eventId) {
		boolean exists = customerService.customerExists(customerId);
		this.sender.sendEvent(new Event(customerExists.succeeded(), new Object[] { exists }, eventId));
	}

	@Override
	public void receiveEvent(Event event) {
		System.out.println("--------------------------------------------------------");
		System.out.println("Event received! : " + event);

		String type = event.getEventType();
		UUID eventId = event.getUUID();

		// TODO: By Jakob: Refactor this to avoid high coupling
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

		System.out.println("--------------------------------------------------------");
	}
}