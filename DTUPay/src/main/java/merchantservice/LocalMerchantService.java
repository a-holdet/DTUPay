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
        return merchant.id;
    }

    public boolean merchantExists(String merchantId){
        if(merchantId==null)
            return false;
        for(Merchant Merchant : merchantRepository.getAllMerchants())
            if(Merchant.id.equals(merchantId))
                return true;
        return false;
    }
}
