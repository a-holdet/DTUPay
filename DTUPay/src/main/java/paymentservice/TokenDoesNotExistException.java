package paymentservice;

public class TokenDoesNotExistException extends Exception {

    public TokenDoesNotExistException(String msg) {
        super(msg);
    }
}