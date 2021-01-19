package bank;

public class BankPortException extends Throwable {
    public BankPortException(String errorMessage) {
        super(errorMessage);
    }
}
