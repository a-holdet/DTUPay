package CustomerMobileApp;

import java.util.List;
import java.util.UUID;

public class DTUPay {

    ITokenGeneration tokenAdapter = new TokenGenerationAdapter();

    public List<UUID> requestNewTokens(Customer customer, int amount) {
        tokenAdapter.createTokensForCustomer(customer, amount);
        return tokenAdapter.readTokensForCustomer(customer);
    }
}
