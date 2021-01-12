package CustomerMobileApp;

import java.util.List;
import java.util.UUID;

public interface ITokenGeneration {

    void createTokensForCustomer(Customer customer, int amount);
    List<UUID> readTokensForCustomer(Customer customer);

}