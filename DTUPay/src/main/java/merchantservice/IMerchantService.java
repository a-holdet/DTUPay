package merchantservice;

import java.util.List;
import java.util.UUID;

public interface IMerchantService {
    String registerMerchant(Merchant merchant) throws IllegalArgumentException;
    Merchant getMerchant(String merchantId);
}
