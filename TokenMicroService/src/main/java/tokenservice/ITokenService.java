package tokenservice;

//import io.quarkus.security.UnauthorizedException;

import java.util.List;
import java.util.UUID;

public interface ITokenService {
//    List<UUID> createTokensForCustomer(String customerId, int amount) throws UnauthorizedException, IllegalTokenGrantingException;

    List<UUID> readTokensForCustomer(String cpr);

    void deleteTokensForCustomer(String cpr);

    String consumeToken(UUID customerToken) throws TokenDoesNotExistException;

    List<UUID> createTokensForCustomer(String customerId, int amount) throws IllegalTokenGrantingException;
}
