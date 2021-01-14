package tokenservice;
import java.util.*;


public interface ITokenRepository {
    void add(UUID token, String customerId);
    List<UUID> getTokensForCustomer(String customerId);
    void deleteTokensForCustomer(String customerId);
    String consumeToken(UUID customerToken);
}
