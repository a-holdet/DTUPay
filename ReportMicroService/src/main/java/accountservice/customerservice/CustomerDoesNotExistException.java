package accountservice.customerservice;

public class CustomerDoesNotExistException extends Exception {
    public CustomerDoesNotExistException(String message) {
        super(message);
    }
}
