package tokenservice.interfaces;

import tokenservice.exceptions.CustomerNotFoundException;

public interface ICustomerService {
    boolean customerExists(String customerId) throws CustomerNotFoundException;
}