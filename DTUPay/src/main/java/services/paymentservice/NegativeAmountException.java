package services.paymentservice;

public class NegativeAmountException extends Throwable {
    public NegativeAmountException(String cannot_transfer_a_negative_amount) {
        super(cannot_transfer_a_negative_amount);
    }
}
