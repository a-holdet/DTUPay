package CustomerMobileApp;

import java.util.List;
import java.util.UUID;

public class DTUPay {

    ITokenGeneration tokenAdapter = new TokenGenerationAdapter();
    //TODO: Refactor to interface?
    CustomerManagementAdapter customerManagementAdapter = new CustomerManagementAdapter();

    public List<UUID> requestNewTokens(Customer customer, int amount) {
        tokenAdapter.createTokensForCustomer(customer, amount);
        return tokenAdapter.readTokensForCustomer(customer);
    }

    public String registerCustomer(Customer customer) {
        return customerManagementAdapter.registerCustomer(customer.getFirstName(), customer.getLastName(), customer.getCprNumber(), customer.getAccountId());
    }

    public void deleteTokensFor(Customer customer) {
        tokenAdapter.deleteTokensFor(customer);
    }
}
