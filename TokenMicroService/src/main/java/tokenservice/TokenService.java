package tokenservice;
import java.util.List;
import java.util.UUID;

public class TokenService implements ITokenService {

    public static TokenService instance = new TokenService();
    public ITokenRepository tokenRepository = new TokenInMemoryRepository();

    @Override
    // Creates tokens for user with 'customerId' iff they are registered at the bank and they have 0 or 1 active tokens.
    public List<UUID> createTokensForCustomer(String customerId, int amount) throws IllegalTokenGrantingException {
        int currentCustomerTokenAmount = readTokensForCustomer(customerId).size();
        if (currentCustomerTokenAmount > 1)
            throw new IllegalTokenGrantingException("Customer cannot request more tokens");
        if (currentCustomerTokenAmount + amount > 6)
            throw new IllegalTokenGrantingException("Customer requested too many tokens");
//        if (!tokenPortAdapter.customerExists(customerId))
//            throw new CustomerNotFoundException("Customer must have a customer id to request tokens");

        for (int i = 0; i < amount; i++) {
            tokenRepository.add(UUID.randomUUID(), customerId);
        }
        return readTokensForCustomer(customerId);
    }

    @Override
    public List<UUID> readTokensForCustomer(String customerId) {
        return tokenRepository.getTokensForCustomer(customerId);
    }

    @Override
    public void deleteTokensForCustomer(String customerId) {
        tokenRepository.deleteTokensForCustomer(customerId);
    }

    @Override
    public String consumeToken(UUID customerToken) throws TokenDoesNotExistException {
        String customerId = tokenRepository.consumeToken(customerToken);
        if (customerId==null)
            throw new TokenDoesNotExistException("token does not exist");
        return customerId;
    }

}