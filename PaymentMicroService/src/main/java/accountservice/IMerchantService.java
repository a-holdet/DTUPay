package accountservice;

/***
 * @Author Jakob Vestergaard Offersen, s163940
 */

public interface IMerchantService {
    Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException;
}
