package merchantservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * @Author Benjamin Wrist Lam, s153486
 */

public class MerchantInMemoryRepository implements IMerchantRepository {
    List<Merchant> merchants =  Collections.synchronizedList(new ArrayList<>());
    @Override
    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    @Override
    public List<Merchant> getAllMerchants() {
        return List.copyOf(merchants);
    }
}
