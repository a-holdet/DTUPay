package merchantservice;

import java.util.ArrayList;
import java.util.List;

public class MerchantInMemoryRepository implements IMerchantRepository {
    List<Merchant> merchants = new ArrayList<>();
    @Override
    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    @Override
    public List<Merchant> getAllMerchants() {
        return merchants;
    }
}
