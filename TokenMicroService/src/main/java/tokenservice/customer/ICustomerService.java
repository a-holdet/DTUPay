package tokenservice.customer;

public interface ICustomerService {
    boolean customerExists(String customerId) throws CustomerNotFoundException;
}