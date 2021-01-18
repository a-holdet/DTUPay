package Accounts;

import DTO.Merchant;

public interface IMerchantService {
    Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException;
}
