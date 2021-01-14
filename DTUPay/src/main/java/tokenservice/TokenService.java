package tokenservice;

import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import io.quarkus.security.UnauthorizedException;

import java.util.*;

public class TokenService implements ITokenService {

    public static TokenService instance = new TokenService();

    public ITokenRepository tokenRepository = new TokenInMemoryRepository();
    private ICustomerService customerService = LocalCustomerService.instance;

    private TokenService() {}

    @Override
    // Creates tokens for user with 'customerId' iff they are registered at the bank and they have 0 or 1 active tokens.
    public List<UUID> createTokensForCustomer(String customerId, int amount) throws UnauthorizedException, IllegalTokenGrantingException {
        if (!customerService.customerExists(customerId)) throw new UnauthorizedException("Customer must have a customer id to request tokens");
        if (readTokensForCustomer(customerId).size() > 1) throw new IllegalTokenGrantingException("Customer cannot request more tokens");

        for (int i = 0; i < amount; i++) {
            tokenRepository.add(UUID.randomUUID(), customerId);
        }
        return readTokensForCustomer(customerId);
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