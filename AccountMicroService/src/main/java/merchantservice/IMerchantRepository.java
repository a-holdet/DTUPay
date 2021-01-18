package merchantservice;

import DTO.Merchant;

import java.util.List;

public interface IMerchantRepository {
    void addMerchant(Merchant merchant);
    List<Merchant> getAllMerchants();
}
