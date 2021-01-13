package customerservice;

import merchantservice.Merchant;

import java.util.List;

public interface ICustomerRepository {
    void addCustomer(Customer customer);
    List<Customer> getAllCustomers();
}
