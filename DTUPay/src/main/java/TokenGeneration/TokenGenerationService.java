package TokenGeneration;

import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import io.quarkus.security.UnauthorizedException;

import java.util.*;

public class TokenGenerationService {

    public static TokenGenerationService instance = new TokenGenerationService();

    private ICustomerService customerService = LocalCustomerService.instance;
    Map<UUID, String> persistence = new HashMap<>();

    private TokenGenerationService() {}

    // Creates tokens for user with 'cpr' iff they are registered at the bank and they have 0 or 1 active tokens.
    public List<UUID> createTokensForCustomer(String customerId, int amount) throws UnauthorizedException {
        if (!customerService.customerExists(customerId)) throw new UnauthorizedException("Customer must have a customer id to request tokens");

        for (int i = 0; i < amount; i++) {
            persistence.put(UUID.randomUUID(), customerId);
        }
        return readTokensForCustomer(customerId);
    }

    public List<UUID> readTokensForCustomer(String customerId) {
        List<UUID> tokens = new ArrayList<>();
        for (UUID uuid : persistence.keySet()) {
            if (persistence.get(uuid).equals(customerId)) tokens.add(uuid);
        }

        return tokens;
    }

    public void deleteTokensForCustomer(String customerId) {
        persistence.entrySet().removeIf(entry -> entry.getValue().equals(customerId));
    }
}