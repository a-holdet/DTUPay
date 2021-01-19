package customerservice;

import java.util.UUID;

/***
 * @Author Benjamin Wrist Lam, s153486
 */

public class LocalCustomerService implements ICustomerService {

    private final ICustomerRepository customerRepository;

    public LocalCustomerService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public String registerCustomer(Customer customer) throws IllegalArgumentException {
        if (!customer.hasValidAccountId())
            throw new IllegalArgumentException("Customer must have a bank account to be created in DTUPay");

        customer.id = String.valueOf(UUID.randomUUID());
        customerRepository.addCustomer(customer);
        return customer.id;
    }

    @Override
    public boolean customerExists(String customerId) {
        for (Customer customer : customerRepository.getAllCustomers()) {
            if (customer.id.equals(customerId)) return true;
        }

        return false;
    }

    @Override
    public Customer getCustomer(String customerId) throws CustomerDoesNotExistException {
        for (Customer customer : customerRepository.getAllCustomers()) {
            if (customer.id.equals(customerId)) return customer;
        }

        throw new CustomerDoesNotExistException("The customer does not exists in DTUPay");
    }
}
