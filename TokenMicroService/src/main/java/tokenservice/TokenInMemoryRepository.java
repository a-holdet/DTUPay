package tokenservice;

import java.util.*;

/***
 * @Author Jakob Vestergaard Offersen, s163940
 */

public class TokenInMemoryRepository implements ITokenRepository {
    Map<UUID, String> tokenCustomerMap = new HashMap<>();

    @Override
    public void add(UUID token, String customerId) {
        tokenCustomerMap.put(UUID.randomUUID(), customerId);
    }

    @Override
    public List<UUID> getTokensForCustomer(String customerId) {
        List<UUID> tokens = new ArrayList<>();
        for (UUID uuid : tokenCustomerMap.keySet()) {
            if (tokenCustomerMap.get(uuid).equals(customerId)) tokens.add(uuid);
        }
        return tokens;
    }

    @Override
    public String consumeToken(UUID customerToken) {
        return tokenCustomerMap.remove(customerToken);
    }
}
