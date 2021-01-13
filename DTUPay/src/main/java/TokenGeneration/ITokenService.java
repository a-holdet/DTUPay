package TokenGeneration;

import java.util.List;
import java.util.UUID;

public interface ITokenService {
    void createTokensForCustomer(String cpr, int amount);

    List<UUID> readTokensForCustomer(String cpr);

    void deleteTokensForCustomer(String cpr);

    String getCustomerId(UUID customerToken);
}
