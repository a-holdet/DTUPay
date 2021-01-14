package CustomerMobileApp;

import CustomerMobileApp.DTO.DTUPayUser;
import CustomerMobileApp.DTO.Payment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

public class MerchantAdapter {
    WebTarget baseUrl;

    public MerchantAdapter(){
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/");
    }

    public String registerMerchant(String firstName, String lastName, String cprNumber, String merchantAccountId) {
        DTUPayUser customer = new DTUPayUser(firstName, lastName, cprNumber, merchantAccountId);
        String merchantId = baseUrl.path("merchants").request().post(Entity.entity(customer, MediaType.APPLICATION_JSON),String.class);
        return merchantId;
    }

    public void transferMoneyFromTo(UUID selectedToken, String merchantId, BigDecimal amount, String description) throws IllegalArgumentException {
        Payment payment = new Payment();
        payment.amount = amount;
        payment.customerToken = selectedToken;
        payment.merchantId = merchantId;
        payment.description = description;

        Response response = baseUrl.path("payments").request().post(Entity.entity(payment, MediaType.APPLICATION_JSON));
        if(response.getStatus()==422){
            String errorMessage = response.readEntity(String.class); //error message is in payload
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }
        response.close();
    }
}
