package AccountService;

public interface IMerchantService {
    String registerMerchant(Merchant merchant) throws IllegalArgumentException;
    Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException;
}
