package messagequeue;

import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import com.google.gson.Gson;

import java.util.List;

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
	public void registerMerchant(Merchant merchant) throws Exception {
		try {
			merchant.id = merchantService.registerMerchant(merchant);
			this.sender.sendEvent(new Event(registerMerchant+"Success",
					new Object[] {merchant}));
		}
		catch (IllegalArgumentException e) {
			this.sender.sendEvent(new Event(registerMerchant+"Fail",
					new Object[] {e.getClass().getSimpleName(), e.getMessage()})
			);
		}
	}

	public static final String getMerchant = "getMerchant";
	public void getMerchant(String merchantId) throws Exception {
		try {
			Merchant merchant = merchantService.getMerchant(merchantId);
			this.sender.sendEvent(new Event(getMerchant+"Success",
					new Object[] {merchant}));
		}
		catch (Exception e) {
			this.sender.sendEvent(new Event(getMerchant+"Fail",
					new Object[] {e.getClass().getSimpleName(), e.getMessage()})
			);
		}
	}

	@Override
	public void receiveEvent(Event event) throws Exception {
		Merchant merchant;
		String merchantId;

		String type = event.getEventType();

		switch (type) {
			case registerMerchant:
				merchant = event.getArgument(0, Merchant.class);
				registerMerchant(merchant);
				break;
			case getMerchant:
				merchantId = event.getArgument(0, String.class);
				getMerchant(merchantId);
				break;
			default:
				//ignore, do nothing
				break;
		}
	}


}