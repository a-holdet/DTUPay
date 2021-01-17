package messagequeue;

import Bank.BankException;
import DTO.Payment;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import paymentservice.*;

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

	private IPaymentService paymentService = PaymentService.getInstance();

	private IEventSender sender;

	public EventService(IEventSender sender) { this.sender = sender; }

	private static final EventType registerPayment = new EventType("registerPayment");

	private void registerPayment(Payment payment, UUID eventID) throws Exception {
		try {
			paymentService.registerPayment(payment);
			Event event = new Event(registerPayment.succeeded(), new Object[] {}, eventID);
			this.sender.sendEvent(event);
		} catch (TokenDoesNotExistException | MerchantDoesNotExistException | CustomerDoesNotExistException | NegativeAmountException | BankException e) {
			String exceptionType = e.getClass().getSimpleName();
			String exceptionMsg = e.getMessage();
			Event event = new Event(registerPayment.failed(), new Object[] {exceptionType, exceptionMsg}, eventID);
			this.sender.sendEvent(event);
		}
	}

	@Override
	public void receiveEvent(Event event) throws Exception {
		System.out.println("--------------------------------------------------------");
		System.out.println("Event received! : " + event);

		String type = event.getEventType();
		UUID eventId = event.getUUID();

		if (type.equals(registerPayment.getName())) {
			Payment payment = event.getArgument(0, Payment.class);
			registerPayment(payment, eventId);
		}
	}
}