package TokenGeneration;

import java.util.*;

public class TokenService implements ITokenService {

    public static TokenService instance = new TokenService();

    public ITokenRepository tokenRepository = new TokenInMemoryRepository();

    private TokenService() {}

    @Override
    public void createTokensForCustomer(String cpr, int amount) {
        for (int i = 0; i < amount; i++) {
            tokenRepository.add(UUID.randomUUID(), cpr);
        }
    }

    @Override
    public List<UUID> readTokensForCustomer(String customerId) {
        List<UUID> tokens = tokenRepository.getTokensForCustomer(customerId);
        return tokens;
    }

    @Override
    public void deleteTokensForCustomer(String customerId) {
        tokenRepository.deleteTokensForCustomer(customerId);
    }

    @Override
    public String getCustomerId(UUID customerToken) {
        return tokenRepository.get(customerToken);
    }
}