package merchantservice;

import java.util.List;
import java.util.UUID;

public interface IMerchantService {
    String registerMerchant(Merchant merchant);

    String getMerchantAccountId(String merchantId);

    Merchant getMerchantWith(String merchantId);

    List<Merchant> getAllMerchants();
}
