package ports;

public class BankException extends Throwable {
    public BankException(String errorMessage) {
        super(errorMessage);
    }
}
