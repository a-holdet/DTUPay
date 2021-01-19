package services.accountservice;

import DTO.Customer;
import DTO.Merchant;

/***
 * @Author Martin Hemmingsen, s141887
 */

public interface IAccountService {
    String registerCustomer(Customer customer) throws IllegalArgumentException;

    String registerMerchant(Merchant merchant) throws IllegalArgumentException;

    Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException;

}
