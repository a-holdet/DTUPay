package CustomerMobileApp;

import CustomerMobileApp.DTO.DTUPayUser;
import CustomerMobileApp.DTO.Payment;
import CustomerMobileApp.DTO.UserReport;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.UUID;

public class MerchantAdapter {
    Client client;
    WebTarget baseUrl;

    public MerchantAdapter(){
        client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/merchantapi");
    }

    public String registerMerchant(String firstName, String lastName, String cprNumber, String merchantAccountId) throws IllegalArgumentException{
        DTUPayUser merchant = new DTUPayUser(firstName, lastName, cprNumber, merchantAccountId);
        Response response = baseUrl.path("merchants").request().post(Entity.entity(merchant, MediaType.APPLICATION_JSON));

        if(response.getStatus()==422){
            String errorMessage = response.readEntity(String.class); //error message is in payload
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }

        String merchantId = response.readEntity(String.class);
        response.close();
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

    public UserReport getMerchantReport(String merchantId) {
        Response response = baseUrl.path("reports").queryParam("id", merchantId).request().get(new GenericType<>() {
        });
        if(response.getStatus() == 422){
            String errorMessage = response.readEntity(String.class); // error message is in payload
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }

        UserReport report  = response.readEntity(new GenericType<>() {});
        return report;

        /*return baseUrl
                .path("reports")
                .queryParam("id", merchantId)
                .request()
                .get(new GenericType<>() {});*/
    }

    public void close() {
        client.close();
    }
}
