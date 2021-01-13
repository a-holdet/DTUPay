package merchantservice;

import java.util.List;

public interface IMerchantRepository {
    public void addMerchant(Merchant merchant);
    public List<Merchant> getAllMerchants();
}
