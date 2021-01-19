package CustomerMobileApp;

import CustomerMobileApp.DTO.DTUPayUser;
import CustomerMobileApp.DTO.Payment;
import CustomerMobileApp.DTO.UserReport;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MerchantPort {
    Client client;
    WebTarget baseUrl;

    public MerchantPort() {
        client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/merchantapi");
    }

    public String registerMerchant(String firstName, String lastName, String cprNumber, String accountId) throws IllegalArgumentException {
        DTUPayUser merchant = new DTUPayUser(firstName, lastName, cprNumber, accountId);
        Response response = baseUrl.path("merchants").request().post(Entity.entity(merchant, MediaType.APPLICATION_JSON));

        if (response.getStatus() == 422) {
            String errorMessage = response.readEntity(String.class);
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }

        String merchantId = response.readEntity(String.class);
        response.close();
        return merchantId;
    }

    public void transferMoneyFromTo(UUID selectedToken, String merchantId, BigDecimal amount, String description) throws IllegalArgumentException, ForbiddenException {
        Payment payment = new Payment(selectedToken, merchantId, description, amount);

        Response response = baseUrl.path("payments").request().post(Entity.entity(payment, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 422) {
            String errorMessage = response.readEntity(String.class);
            response.close();
            throw new IllegalArgumentException(errorMessage);
        } else if (response.getStatus() == 403) {
            String errorMessage = response.readEntity(String.class);
            response.close();
            throw new ForbiddenException(errorMessage);
        }
        response.close();
    }

    public UserReport getMerchantReport(String merchantId, LocalDateTime start, LocalDateTime end) {
        if (start == null) start = LocalDateTime.MIN;
        if (end == null) end = LocalDateTime.MAX;
        Response response = baseUrl.path("reports").queryParam("id", merchantId).queryParam("start", start
                .toString()).queryParam("end", end.toString()).request().get(new GenericType<>() {
        });


        if (response.getStatus() == 422) {
            String errorMessage = response.readEntity(String.class); // error message is in payload
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }
        UserReport userReport = response.readEntity(UserReport.class);
        return userReport;
    }

    public void close() {
        client.close();
    }
}
