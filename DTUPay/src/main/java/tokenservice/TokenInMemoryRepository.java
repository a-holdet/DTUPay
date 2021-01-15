package tokenservice;

import java.util.*;

public class TokenInMemoryRepository implements ITokenRepository{
    Map<UUID, String> persistence = new HashMap<>();

    @Override
    public void add(UUID token, String customerId) {
        persistence.put(UUID.randomUUID(), customerId);
    }

    @Override
    public List<UUID> getTokensForCustomer(String customerId) {
        List<UUID> tokens = new ArrayList<>();
        for (UUID uuid : persistence.keySet()) {
            if (persistence.get(uuid).equals(customerId)) tokens.add(uuid);
        }
        return tokens;
    }

    @Override
    public String consumeToken(UUID customerToken) {
        String customerId = persistence.remove(customerToken);
        return customerId;
    }
}
