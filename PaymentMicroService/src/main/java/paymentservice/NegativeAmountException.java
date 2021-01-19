package paymentservice;

public class NegativeAmountException extends Exception {
    public NegativeAmountException(String cannot_transfer_a_negative_amount) {
        super(cannot_transfer_a_negative_amount);
    }
}
