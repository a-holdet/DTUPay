package merchantservice;

import java.util.UUID;

public interface IMerchantService {
    String registerMerchant(Merchant merchant);

    boolean merchantExists(String merchantId);
}
