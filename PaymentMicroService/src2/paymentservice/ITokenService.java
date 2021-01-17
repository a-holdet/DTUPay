package paymentservice;

import java.util.List;
import java.util.UUID;

public interface ITokenService {
    List<UUID> createTokensForCustomer(String customerId, int amount) throws UnauthorizedException, IllegalTokenGrantingException;

    List<UUID> readTokensForCustomer(String cpr);

    String consumeToken(UUID customerToken) throws TokenDoesNotExistException;
}
