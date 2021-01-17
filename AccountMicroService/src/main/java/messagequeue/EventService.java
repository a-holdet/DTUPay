package messagequeue;

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

	static final IMerchantService merchantService = LocalMerchantService.instance;
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

	public static final String getMerchant = "getMerchant";
	public void getMerchant(String merchantId, UUID eventID) throws Exception {
		try {
			Merchant merchant = merchantService.getMerchant(merchantId);
			Event event = new Event(getMerchant+"Success", new Object[] {merchant}, eventID);;
			this.sender.sendEvent(event);
		}
		catch (MerchantDoesNotExistException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(getMerchant+"Fail", new Object[] {exceptionType, exceptionMsg}, eventID);
			this.sender.sendEvent(event);
		}
	}

	@Override
	public void receiveEvent(Event event) throws Exception {
		System.out.println("--------------------------------------------------------");
		System.out.println("Event received! : " + event);

		Merchant merchant;
		String merchantId;

		String type = event.getEventType();
		UUID eventId = event.getUUID();
		try {
			switch (type) {
				case registerMerchant:
					merchant = event.getArgument(0, Merchant.class);
					registerMerchant(merchant, eventId);
					break;
				case getMerchant:
					merchantId = event.getArgument(0, String.class);
					getMerchant(merchantId, eventId);
					break;
				default:
					//ignore, do nothing
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("--------------------------------------------------------");
	}


}