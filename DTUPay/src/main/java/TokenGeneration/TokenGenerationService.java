package TokenGeneration;

import java.util.*;

public class TokenGenerationService {

    public static TokenGenerationService instance = new TokenGenerationService();

    Map<UUID, String> persistence = new HashMap<>();

    private TokenGenerationService() {}

    public void createTokensForCustomer(String cpr, int amount) {
        for (int i = 0; i < amount; i++) {
            persistence.put(UUID.randomUUID(), cpr);
        }
    }

    public List<UUID> readTokensForCustomer(String cpr) {
        List<UUID> tokens = new ArrayList<>();
        for (UUID uuid : persistence.keySet()) {
            if (persistence.get(uuid).equals(cpr)) tokens.add(uuid);
        }

        return tokens;
    }

    public void deleteTokensForCustomer(String cpr) {
        persistence.entrySet().removeIf(entry -> entry.getValue().equals(cpr));
    }
}