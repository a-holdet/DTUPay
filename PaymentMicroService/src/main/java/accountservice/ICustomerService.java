package accountservice;

public interface ICustomerService {
    Customer getCustomer(String customerId) throws CustomerDoesNotExistException;
}
