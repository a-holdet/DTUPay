package paymentservice;

public class IllegalTokenGrantingException extends Throwable {
    public IllegalTokenGrantingException(String message) {
        super(message);
    }
}