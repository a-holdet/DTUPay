package accountservice;

import accountservice.CustomerNotFoundException;

public interface ICustomerService {
    boolean customerExists(String customerId) throws CustomerNotFoundException;
}