package accountservice;

import DTO.Customer;
import DTO.Merchant;

public interface IAccountService {
    String registerCustomer(Customer customer) throws IllegalArgumentException;

    boolean customerExists(String customerId);

    Customer getCustomer(String customerId) throws CustomerDoesNotExistException;

    String registerMerchant(Merchant merchant) throws IllegalArgumentException;

    Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException;

}
