package merchantservice;

import java.util.UUID;

public class LocalMerchantService implements IMerchantService{
    public static LocalMerchantService instance = new LocalMerchantService();
    IMerchantRepository merchantRepository = new MerchantInMemoryRepository();

    public String registerMerchant(Merchant merchant){
        if(merchant.accountId==null || merchant.accountId.length()==0)
            throw new IllegalArgumentException("Merchant must have an account id to be created in DTUPay");
        merchant.id = String.valueOf(UUID.randomUUID());
        merchantRepository.addMerchant(merchant);
        System.out.println("REGISTER MERCHANT WITH ID" + merchant.id);
        System.out.println("ALL MERCHANTS: " + merchantRepository.getAllMerchants().size());
        return merchant.id;
    }

    public boolean merchantExists(String merchantId){
        return getMerchantAccountId(merchantId) != null;
    }

    @Override
    public String getMerchantAccountId(String merchantId) {
        if(merchantId==null)
            return null;
        for(Merchant merchant : merchantRepository.getAllMerchants())
            if(merchant.id.equals(merchantId))
                return merchant.accountId;
        return null;
    }

    @Override
    public Merchant getMerchantWith(String merchantId) {
        System.out.println("GET MERCHANT WITH ID: " + merchantId);
        System.out.println("ALL MERCHANTS: " + merchantRepository.getAllMerchants().size());
        System.out.println(merchantRepository.getAllMerchants());
        for (Merchant m: merchantRepository.getAllMerchants()) {
            if (m.id.equals(merchantId)) return m;
        }
        return null;
    }
}
