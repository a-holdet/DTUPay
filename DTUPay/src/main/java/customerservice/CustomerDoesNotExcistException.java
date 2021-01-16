package customerservice;

public class CustomerDoesNotExcistException extends Exception {
    public CustomerDoesNotExcistException(String customerId) {
        super("The customer '" + customerId + "' does not exist in DTUPay");
    }
}
