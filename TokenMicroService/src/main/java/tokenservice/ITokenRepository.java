package tokenservice;

import java.util.List;
import java.util.UUID;

public interface ITokenRepository {

    void add(UUID token, String customerId);

    List<UUID> getTokensForCustomer(String customerId);

    String consumeToken(UUID customerToken);
}
