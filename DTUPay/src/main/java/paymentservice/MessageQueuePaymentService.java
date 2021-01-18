package paymentservice;

import Bank.BankException;
import DTO.Payment;
import customerservice.CustomerDoesNotExistException;
import merchantservice.EventType;
import merchantservice.MerchantDoesNotExistException;
import messagequeuebase.MessageQueueBase;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventQueue;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import tokenservice.ConsumeTokenException;
import tokenservice.TokenDoesNotExistException;

import java.util.function.DoubleToIntFunction;

public class MessageQueuePaymentService extends MessageQueueBase implements IPaymentService {

    private static MessageQueuePaymentService instance;

    public static MessageQueuePaymentService getInstance() {
        if (instance == null) {
            try {
                var ies = EventExchange.instance.getSender();
                MessageQueuePaymentService service = new MessageQueuePaymentService(ies);
                new EventQueue(service).startListening();
                instance = service;
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        return instance;
    }

    public MessageQueuePaymentService(IEventSender sender) {
        super(sender);
        supportedEventTypes =new EventType[] {registerPayment};
        instance = this;
    }

    private final EventType registerPayment = new EventType("registerPayment");


    @Override
    public void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException, BankException, CustomerDoesNotExistException, ConsumeTokenException {
        System.out.println("REGISTER PAYMENT: ");
        Event response = sendRequestAndAwaitReponse(payment, registerPayment);

        System.out.println("REGISTER PAYMENT2: ");
        if (response.isFailureReponse()) {
            String exceptionType = response.getArgument(0, String.class); //TODO: Refactor into own method on "response"
            String exceptionMessage = response.getErrorMessage();
            switch (exceptionType) {
                case "MerchantDoesNotExistException": throw new MerchantDoesNotExistException(exceptionMessage);
                case "NegativeAmountException": throw new NegativeAmountException(exceptionMessage);
                case "BankException": throw new BankException(exceptionMessage);
                case "ConsumeTokenException": throw new ConsumeTokenException(exceptionMessage);
                case "TokenDoesNotExistException": throw new TokenDoesNotExistException(exceptionMessage);
                case "CustomerDoesNotExistException": throw new CustomerDoesNotExistException(exceptionMessage);
            }
        }
    }
}
