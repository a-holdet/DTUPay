package tokenservice;

//import io.quarkus.security.UnauthorizedException;

import java.util.List;
import java.util.UUID;

public interface ITokenService {

//    List<UUID> createTokensForCustomer(String customerId, int amount) throws CustomerNotFoundException, IllegalTokenGrantingException;

    List<UUID> readTokensForCustomer(String cpr);

    String consumeToken(UUID customerToken) throws TokenDoesNotExistException, Exception, ConsumeTokenException;

    List<UUID> createTokensForCustomer(String customerId, int amount) throws IllegalTokenGrantingException, Exception;
}
