package tokenservice;

import accountservice.CustomerNotFoundException;
import accountservice.ICustomerService;

import java.util.List;
import java.util.UUID;

/***
 * @Author Sebastian Arcos Specht, s164394
 */

public class LocalTokenService implements ITokenService {
    private final ITokenRepository tokenRepository;
    private final ICustomerService customerService;

    public LocalTokenService(ICustomerService customerService, ITokenRepository tokenRepository) {
        this.customerService = customerService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<UUID> createTokensForCustomer(String customerId, int amount) throws IllegalTokenGrantingException, CustomerNotFoundException {
        int currentCustomerTokenAmount = readTokensForCustomer(customerId).size();

        if (currentCustomerTokenAmount > 1)
            throw new IllegalTokenGrantingException("Customer cannot request more tokens");
        if (currentCustomerTokenAmount + amount > 6)
            throw new IllegalTokenGrantingException("Customer requested too many tokens");
        if (customerId == null || !customerService.customerExists(customerId))
            throw new CustomerNotFoundException("Customer must have a customer id to request tokens");

        for (int i = 0; i < amount; i++)
            tokenRepository.add(UUID.randomUUID(), customerId);

        return readTokensForCustomer(customerId);
    }

    /// Returns the id for the customer who owns the customer token. The token is also removed, so that the token cannot be re-used.
    @Override
    public String consumeToken(UUID customerToken) throws TokenDoesNotExistException {
        String customerId = tokenRepository.consumeToken(customerToken);
        if (customerId == null)
            throw new TokenDoesNotExistException("Token does not exist");

        return customerId;
    }

    private List<UUID> readTokensForCustomer(String customerId) {
        return tokenRepository.getTokensForCustomer(customerId);
    }
}