package tokenservice;

public class CustomerNotFoundException extends Exception {
    CustomerNotFoundException(String message) {
        super(message);
    }
}
