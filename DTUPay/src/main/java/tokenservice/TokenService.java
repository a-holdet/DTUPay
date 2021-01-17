package tokenservice;

import customerservice.ICustomerService;
import customerservice.LocalCustomerService;
import io.quarkus.security.UnauthorizedException;
import merchantservice.MessageQueueAccountService;

import java.util.*;

public class TokenService implements ITokenService {

    public static TokenService instance = new TokenService();

    private final ITokenRepository tokenRepository;
    private final ICustomerService customerService;

    public TokenService() {
        this(new TokenInMemoryRepository(), MessageQueueAccountService.getInstance());
    }

    public TokenService(ITokenRepository tokenRepository, ICustomerService customerService) {
        this.tokenRepository = tokenRepository;
        this.customerService = customerService;
    }

    @Override
    // Creates tokens for user with 'customerId' iff they are registered at the bank and they have 0 or 1 active tokens.
    public List<UUID> createTokensForCustomer(String customerId, int amount) throws UnauthorizedException, IllegalTokenGrantingException {
        if (!customerService.customerExists(customerId)) throw new UnauthorizedException("Customer must have a customer id to request tokens");

        int currentCustomerTokenAmount = readTokensForCustomer(customerId).size();
        if (currentCustomerTokenAmount > 1) throw new IllegalTokenGrantingException("Customer cannot request more tokens");
        if (currentCustomerTokenAmount + amount > 6) throw new IllegalTokenGrantingException("Customer requested too many tokens");

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
    /// Returns the customer id of customer owning 'customerToken' or throws if the customer does not exist
    public String consumeToken(UUID customerToken) throws TokenDoesNotExistException {
        return tokenRepository.consumeToken(customerToken);
    }
}