package merchantservice;

import java.util.UUID;

public class LocalMerchantService implements IMerchantService {
    public static LocalMerchantService instance = new LocalMerchantService();
    IMerchantRepository merchantRepository = new MerchantInMemoryRepository();

    @Override
    public String registerMerchant(Merchant merchant) throws IllegalArgumentException {
        if(merchant.accountId==null || merchant.accountId.length()==0)
            throw new IllegalArgumentException("Merchant must have a bank account to be created in DTUPay");
        merchant.id = String.valueOf(UUID.randomUUID());
        merchantRepository.addMerchant(merchant);
        return merchant.id;
    }

    @Override
    public Merchant getMerchant(String merchantId) throws IllegalArgumentException {
        for (Merchant m: merchantRepository.getAllMerchants()) {
            if (m.id.equals(merchantId)) return m;
        }
        throw new IllegalArgumentException("Merchant with id "+merchantId+" does not exist");
    }
}
