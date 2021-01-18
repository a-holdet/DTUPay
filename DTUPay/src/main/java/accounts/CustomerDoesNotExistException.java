package accounts;

public class CustomerDoesNotExistException extends Exception {
    public CustomerDoesNotExistException(String message) {
        super(message);
    }
}
