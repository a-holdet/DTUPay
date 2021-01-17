package messagequeue;

import customerservice.Customer;
import customerservice.CustomerDoesNotExistException;
import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import merchantservice.MerchantDoesNotExistException;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

public class EventService implements IEventReceiver {

	// Singleton as method due to serviceTest
	private static EventService instance;
	public static EventService getInstance() {
		if (instance == null) {
			try {
				System.out.println("event service parent channel");
				System.out.println(EventExchange.instance.parentChannel);
				var ies = EventExchange.instance.getSender();
				EventService service = new EventService(ies);
				new EventQueue().registerReceiver(service);
				instance = service;
			} catch (Exception e) {
				throw new Error(e);
			}
		}
		return instance;
	}

	private static final IMerchantService merchantService = LocalMerchantService.instance;
	private static final ICustomerService customerService = LocalCustomerService.instance;
	IEventSender sender;

	public EventService(IEventSender sender) { this.sender = sender; }

	public static final String registerMerchant = "registerMerchant";
	public void registerMerchant(Merchant merchant, UUID eventID) throws Exception {
		try {
			String merchantId = merchantService.registerMerchant(merchant);
			Event event = new Event(registerMerchant+"Success", new Object[] {merchantId}, eventID);
			this.sender.sendEvent(event);
		}
		catch (IllegalArgumentException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(registerMerchant+"Fail", new Object[] {exceptionType, exceptionMsg}, eventID);
			this.sender.sendEvent(event);
		}
	}

	public static final String registerCustomer = "registerCustomer";
	public void registerCustomer(Customer customer, UUID eventID) throws Exception {
		try {
			String customerId = customerService.registerCustomer(customer);
			Event event = new Event(registerCustomer+"Success", new Object[] {customerId}, eventID);
			this.sender.sendEvent(event);
		}
		catch (IllegalArgumentException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(registerCustomer+"Fail", new Object[] {exceptionType, exceptionMsg}, eventID);
			this.sender.sendEvent(event);
		}
	}

	public static final String getMerchant = "getMerchant";
	public void getMerchant(String merchantId, UUID eventID) throws Exception {
		try {
			Merchant merchant = merchantService.getMerchant(merchantId);
			Event event = new Event(getMerchant+"Success", new Object[] {merchant}, eventID);
			this.sender.sendEvent(event);
		}
		catch (MerchantDoesNotExistException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(getMerchant+"Fail", new Object[] {exceptionType, exceptionMsg}, eventID);
			this.sender.sendEvent(event);
		}
	}

	public static final String getCustomer = "getCustomer";
	public void getCustomer(String customerId, UUID eventID) throws Exception {
		try {
			Customer customer = customerService.getCustomer(customerId);
			Event event = new Event(getCustomer+"Success", new Object[] {customer}, eventID);
			this.sender.sendEvent(event);
		} catch (CustomerDoesNotExistException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(getCustomer+"Fail", new Object[] {exceptionType, exceptionMsg}, eventID);
			this.sender.sendEvent(event);
		}
	}

	public static final String customerExists = "customerExists";
	public void customerExists(String customerId, UUID eventId) throws Exception {
		boolean exists = customerService.customerExists(customerId);
		this.sender.sendEvent(new Event(customerExists+"Success", new Object[]{exists}, eventId));
	}

	@Override
	public void receiveEvent(Event event) throws Exception {
		System.out.println("--------------------------------------------------------");
		System.out.println("Event received! : " + event);

		String type = event.getEventType();
		UUID eventId = event.getUUID();

		try {
			switch (type) {
				case registerMerchant:
					Merchant merchant = event.getArgument(0, Merchant.class);
					registerMerchant(merchant, eventId);
					break;
				case getMerchant:
					String merchantId = event.getArgument(0, String.class);
					getMerchant(merchantId, eventId);
					break;
				case getCustomer:
					String customerId = event.getArgument(0, String.class);
					getCustomer(customerId, eventId);
					break;
				case registerCustomer:
					Customer customer = event.getArgument(0, Customer.class);
					registerCustomer(customer, eventId);
					break;
				case customerExists:
					String id = event.getArgument(0, String.class);
					customerExists(id, eventId);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("--------------------------------------------------------");
	}
}