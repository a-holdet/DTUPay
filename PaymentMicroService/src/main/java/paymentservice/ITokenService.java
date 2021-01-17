package paymentservice;

import java.util.UUID;

public interface ITokenService {
    String consumeToken(UUID customerToken) throws TokenDoesNotExistException;
}
