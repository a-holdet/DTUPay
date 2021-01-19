package accountservice;

public interface IMerchantService {
    Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException;
}
