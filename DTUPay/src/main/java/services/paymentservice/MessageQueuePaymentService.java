package services.paymentservice;

import DTO.Payment;
import services.accountservice.CustomerDoesNotExistException;
import services.accountservice.MerchantDoesNotExistException;
import messaging.rmq.event.objects.MessageQueueBase;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;
import services.tokenservice.TokenDoesNotExistException;

public class MessageQueuePaymentService extends MessageQueueBase implements IPaymentService {

    private static final EventType registerPayment = new EventType("registerPayment");
    private static final EventType[] supportedEventTypes = new EventType[] {registerPayment};

    public MessageQueuePaymentService(IEventSender sender) {
        super(sender);
    }

    @Override
    public EventType[] getSupportedEventTypes() {
        return supportedEventTypes;
    }

    @Override
    public void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException, BankException, CustomerDoesNotExistException {
        Event response = sendRequestAndAwaitResponse(payment, registerPayment);
        if (response.isFailureReponse()) {
            String exceptionType = response.getErrorType();
            String exceptionMessage = response.getErrorMessage();
            switch (exceptionType) {
                case "MerchantDoesNotExistException": throw new MerchantDoesNotExistException(exceptionMessage);
                case "NegativeAmountException": throw new NegativeAmountException(exceptionMessage);
                case "BankException": throw new BankException(exceptionMessage);
                case "TokenDoesNotExistException": throw new TokenDoesNotExistException(exceptionMessage);
                case "CustomerDoesNotExistException": throw new CustomerDoesNotExistException(exceptionMessage);
            }
        }
    }
}
