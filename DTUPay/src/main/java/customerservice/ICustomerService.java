package customerservice;

public interface ICustomerService {
    String registerCustomer(Customer customer) throws IllegalArgumentException;

    boolean customerExists(String customerId);

    String getCustomerAccountId(String customerId);

    Customer getCustomerWith(String customerId);
}
