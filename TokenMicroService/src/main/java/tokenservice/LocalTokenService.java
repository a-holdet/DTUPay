package tokenservice;
import tokenservice.customer.CustomerNotFoundException;
import tokenservice.customer.ICustomerService;

import java.util.List;
import java.util.UUID;

public class LocalTokenService implements ITokenService {
    public ITokenRepository tokenRepository = new TokenInMemoryRepository();
    private final ICustomerService customerService;

    public LocalTokenService(ICustomerService customerService) {
        this.customerService = customerService;
    }


    @Override
    // Creates tokens for user with 'customerId' iff they are registered at the bank and they have 0 or 1 active tokens.
    public List<UUID> createTokensForCustomer(String customerId, int amount) throws IllegalTokenGrantingException, CustomerNotFoundException {
        System.out.println("createtokensforcustomer first");
        int currentCustomerTokenAmount = readTokensForCustomer(customerId).size();
        if (currentCustomerTokenAmount > 1)
            throw new IllegalTokenGrantingException("Customer cannot request more tokens");
        if (currentCustomerTokenAmount + amount > 6)
            throw new IllegalTokenGrantingException("Customer requested too many tokens");
        if (!customerService.customerExists(customerId))
            throw new CustomerNotFoundException("Customer must have a customer id to request tokens");
        System.out.println("createtokensforcustomer second");

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