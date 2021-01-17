package paymentservice;

import java.util.List;
import java.util.UUID;

public class TokenServiceMock implements ITokenService {
    @Override
    public List<UUID> createTokensForCustomer(String customerId, int amount) throws UnauthorizedException, IllegalTokenGrantingException {
        return null;
    }

    @Override
    public List<UUID> readTokensForCustomer(String cpr) {
        return null;
    }

    @Override
    public String consumeToken(UUID customerToken) throws TokenDoesNotExistException {
        return null;
    }
}
