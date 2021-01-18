package messagequeue;

import Accounts.CustomerDoesNotExistException;
import Accounts.MerchantDoesNotExistException;
import Bank.BankException;
import DTO.Payment;
import Tokens.TokenDoesNotExistException;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import messaging.rmq.event.objects.EventType;
import paymentservice.*;

import java.util.UUID;

public class EventService implements IEventReceiver {

	// Singleton as method due to serviceTest
	private static EventService instance;
	public static EventService getInstance() {
		if (instance == null) {
			try {
				var ies = new EventExchangeFactory().getExchange().getSender();
				EventService service = new EventService(ies);
				new EventQueue(service).startListening();
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
			System.out.println("register payment: " + payment);
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
	public void receiveEvent(Event event) {
		System.out.println("--------------------------------------------------------");


		String type = event.getEventType();
		UUID eventId = event.getUUID();

		if (type.equals(registerPayment.getName())) {
			System.out.println("Supported Event received! : " + event);
			Payment payment = event.getArgument(0, Payment.class);
			new Thread(() -> {
				try {
					registerPayment(payment, eventId);
				} catch (Exception e) {
					e.printStackTrace(); // TODO: Fix properly
				}
			}).start();
		} else {
			System.out.println("Event received! : " + event);
		}
	}
}