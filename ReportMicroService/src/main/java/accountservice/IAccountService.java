package accountservice;

/***
 * @Author Benjamin Wrist Lam, s153486
 */

public interface IAccountService {
    String registerCustomer(Customer customer) throws IllegalArgumentException;

    Customer getCustomer(String customerId) throws CustomerDoesNotExistException;

    Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException;
}
