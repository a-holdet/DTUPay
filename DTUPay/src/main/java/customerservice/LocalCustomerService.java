package customerservice;

import merchantservice.Merchant;

import java.util.UUID;

public class LocalCustomerService implements ICustomerService {
    public static ICustomerService instance = new LocalCustomerService();
    ICustomerRepository customerRepository = new CustomerInMemoryRepository();

    public LocalCustomerService(){}

    @Override
    public String registerCustomer(Customer customer) throws IllegalArgumentException {
        if(customer.accountId==null || customer.accountId.length()==0)
            throw new IllegalArgumentException("Customer must have a bank account to be created in DTUPay");
        customer.id = String.valueOf(UUID.randomUUID());
        customerRepository.addCustomer(customer);
        return customer.id;
    }

    @Override
    public boolean customerExists(String customerId){
        if(customerId==null)
            return false;
        for(Customer customer : customerRepository.getAllCustomers())
            if(customer.id.equals(customerId))
                return true;
        return false;
    }

    @Override
    public String getCustomerAccountId(String customerId) {
        if(customerId==null)
            return null;
        for(Customer customer : customerRepository.getAllCustomers())
            if(customer.id.equals(customerId))
                return customer.accountId;
        return null;
    }

    @Override
    public Customer getCustomerWith(String customerId) {
        if(customerId==null)
            return null;
        for(Customer customer : customerRepository.getAllCustomers())
            if(customer.id.equals(customerId))
                return customer;
        return null;
    }
}
