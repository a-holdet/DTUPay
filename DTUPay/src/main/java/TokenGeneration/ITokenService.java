package TokenGeneration;

import io.quarkus.security.UnauthorizedException;

import java.util.List;
import java.util.UUID;

public interface ITokenService {
    List<UUID> createTokensForCustomer(String customerId, int amount) throws UnauthorizedException;

    List<UUID> readTokensForCustomer(String cpr);

    void deleteTokensForCustomer(String cpr);

    String getCustomerId(UUID customerToken);
}
