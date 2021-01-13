package CustomerMobileApp;

import dtu.ws.fastmoney.User;

import java.util.List;
import java.util.UUID;

//TODO: For mow we use 'cpr' as id for a user.
public interface ITokenGeneration {

    void createTokensForCustomer(User customer, int amount);
    List<UUID> readTokensForCustomer(User customer);

    void deleteTokensFor(User customer);
}