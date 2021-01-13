package CustomerMobileApp;

import dtu.ws.fastmoney.User;
import io.quarkus.security.UnauthorizedException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class DTUPay {

    TokenGenerationAdapter tokenAdapter = new TokenGenerationAdapter();
    UserManagementAdapter userManagementAdapter = new UserManagementAdapter();
    PaymentAdapter paymentAdapter = new PaymentAdapter();

    public List<UUID> requestNewTokens(User customer, int amount) throws UnauthorizedException {
        tokenAdapter.createTokensForCustomer(customer, amount);
        return tokenAdapter.readTokensForCustomer(customer);
    }

    public String registerCustomer(User customer, String accountID) {
        return userManagementAdapter.registerCustomer(customer.getFirstName(), customer.getLastName(), customer.getCprNumber(), accountID);
    }

    public String registerMerchant(User merchant, String accountID) {
        return userManagementAdapter.registerMerchant(merchant.getFirstName(), merchant.getLastName(), merchant.getCprNumber(), accountID);
    }

    public void deleteTokensFor(User customer) {
        tokenAdapter.deleteTokensFor(customer);
    }

    public void transferMoneyFromTo(String customerAccountId, String merchantId, BigDecimal amount, String description) throws Exception {
        paymentAdapter.transferMoneyFromTo(customerAccountId, merchantId, amount, description);
    }
}
