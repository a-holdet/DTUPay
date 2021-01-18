package tokenservice.interfaces;

import tokenservice.exceptions.CustomerNotFoundException;
import tokenservice.exceptions.IllegalTokenGrantingException;
import tokenservice.exceptions.TokenDoesNotExistException;

import java.util.List;
import java.util.UUID;

public interface ITokenService {

    String consumeToken(UUID customerToken) throws TokenDoesNotExistException;

    List<UUID> createTokensForCustomer(String customerId, int amount) throws IllegalTokenGrantingException, CustomerNotFoundException;

}
