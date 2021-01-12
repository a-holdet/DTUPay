package BusinessLogic;

public class CustomerDoesNotExistException extends Throwable {
    String customerId;
    public CustomerDoesNotExistException(String customerId) {
        this.customerId=customerId;
    }

    @Override
    public String getMessage() {
        return "customer with id " + customerId + " is unknown";
    }
}
