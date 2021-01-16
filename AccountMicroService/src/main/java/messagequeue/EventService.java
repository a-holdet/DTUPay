package messagequeue;

import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class EventService implements IEventReceiver {

	public static EventService instance = null; // startUp(); //cannot be used until after startUp();

	public static void startUp() {
		if (instance == null) {
			try {
				var s = EventExchange.instance.getSender();
				EventService service = new EventService(s);
				new EventQueue().registerReceiver(service);
				instance = service;
			} catch (Exception e) {
				throw new Error(e);
			}
		}
	}

	IEventSender sender;
	IMerchantService merchantService = LocalMerchantService.instance;

	public EventService(IEventSender sender) { this.sender = sender; }

	@Override
	public void receiveEvent(Event event) throws Exception {
		String type = event.getEventType();
		Object[] arguments = event.getArguments();
		Merchant merchant;
		switch (type) {
			case "registerMerchant":
				merchant = (Merchant) arguments[0];
				try {
					merchant.id = merchantService.registerMerchant(merchant);
					this.sender.sendEvent(new Event(type+"Success",
							new Object[] {merchant}));
				}
				catch (IllegalArgumentException e) {
					this.sender.sendEvent(new Event(type+"Fail",
							new Object[] {e.getClass().getSimpleName(), e.getMessage()})
					);
				}
				break;
			case "getMerchant":
				String merchantId = (String) arguments[0];
				try {
					merchant = merchantService.getMerchant(merchantId);
					this.sender.sendEvent(new Event(type+"Success",
							new Object[] {merchant}));
				}
				catch (Exception e) {
					this.sender.sendEvent(new Event(type+"Fail",
							new Object[] {e.getClass().getSimpleName(), e.getMessage()})
					);
				}
				break;
			case "RabbitTest a":
				Event e = new Event("RabbitTest b");
				this.sender.sendEvent(e);
				break;
			default:
				//ignore, do nothing
		}
	}
}