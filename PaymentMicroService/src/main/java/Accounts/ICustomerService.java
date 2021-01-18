package Accounts;

import DTO.Customer;

public interface ICustomerService {
    Customer getCustomer(String customerId) throws CustomerDoesNotExistException;
}
