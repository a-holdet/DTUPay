package customerservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerInMemoryRepository implements ICustomerRepository {
    List<Customer> customers = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return List.copyOf(customers);
    }
}
