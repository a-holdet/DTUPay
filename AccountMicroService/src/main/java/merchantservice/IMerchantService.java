package merchantservice;

import DTO.Merchant;

public interface IMerchantService {
    String registerMerchant(Merchant merchant) throws IllegalArgumentException;
    Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException;
}
