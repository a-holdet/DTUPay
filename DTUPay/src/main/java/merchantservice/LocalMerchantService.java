package merchantservice;

import java.util.UUID;

public class LocalMerchantService implements IMerchantService{
    public static LocalMerchantService instance = new LocalMerchantService();

    private IMerchantRepository merchantRepository;

    public LocalMerchantService() {
        this(new MerchantInMemoryRepository());
    }

    public LocalMerchantService(IMerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Override
    public String registerMerchant(Merchant merchant) throws IllegalArgumentException {
        if (merchant.accountId==null || merchant.accountId.isEmpty())
            throw new IllegalArgumentException("Merchant must have a bank account to be created in DTUPay");

        merchant.id = String.valueOf(UUID.randomUUID());
        merchantRepository.addMerchant(merchant);

        return merchant.id;
    }

    @Override
    public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
        for (Merchant m: merchantRepository.getAllMerchants()) {
            if (m.id.equals(merchantId)) return m;
        }

        throw new MerchantDoesNotExistException(merchantId);
    }
}
