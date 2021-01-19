package messagequeue;

import accountservice.CustomerDoesNotExistException;
import accountservice.MerchantDoesNotExistException;
import bank.BankException;
import paymentservice.Payment;
import tokenservice.TokenDoesNotExistException;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.objects.EventType;
import messaging.rmq.event.interfaces.IEventReceiver;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import paymentservice.*;

import java.util.UUID;

public class MessageQueueConnector implements IEventReceiver {

	private IPaymentService paymentService;
	private IEventSender sender;

	public MessageQueueConnector(IPaymentService paymentService, IEventSender sender) {
		this.sender = sender;
		this.paymentService = paymentService;
	}

	private final EventType registerPayment = new EventType("registerPayment");

	@Override
	public EventType[] getSupportedEventTypes() {
		return new EventType[] {registerPayment};
	}

	private void registerPayment(Payment payment, UUID eventID) {
		try {
			paymentService.registerPayment(payment);
			Event event = new Event(registerPayment.succeeded(), new Object[] {}, eventID);
			this.sender.sendEvent(event);
		} catch (TokenDoesNotExistException | MerchantDoesNotExistException | CustomerDoesNotExistException | NegativeAmountException | BankException e) {
			this.sender.sendErrorEvent(registerPayment, e, eventID);
		}
	}

	@Override
	public void receiveEvent(Event event) {
		String type = event.getEventType();
		UUID eventId = event.getUUID();

		if (type.equals(registerPayment.getName())) {
			Payment payment = event.getArgument(0, Payment.class);
			registerPayment(payment, eventId);
		}
	}
}