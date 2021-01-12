package BusinessLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerService {
    public static CustomerService instance = new CustomerService();
    List<Customer> customers = new ArrayList<>();

    public CustomerService(){}

    public String registerCustomer(Customer customer) throws IllegalArgumentException {
        if(customer.accountId==null || customer.accountId.length()==0)
            throw new IllegalArgumentException("Customer must have an account id to be created in DTUPay");
        customer.id = String.valueOf(UUID.randomUUID());
        customers.add(customer);
        return customer.id;
    }

    public boolean customerExists(String customerId){
        if(customerId==null)
            return false;
        for(Customer customer : customers)
            if(customer.id.equals(customerId))
                return true;
        return false;
    }
}
