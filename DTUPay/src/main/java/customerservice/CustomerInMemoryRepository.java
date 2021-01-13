package customerservice;

import merchantservice.Merchant;

import java.util.ArrayList;
import java.util.List;

public class CustomerInMemoryRepository implements ICustomerRepository {
    List<Customer> customers = new ArrayList<>();

    @Override
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers;
    }
}
