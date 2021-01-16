package reportservice;

public class CustomerDoesNotExistsException extends Throwable {
    public CustomerDoesNotExistsException(String message) {
        super(message);
    }
}
