package CustomerMobileApp;

import CustomerMobileApp.DTO.DTUPayUser;
import CustomerMobileApp.DTO.TokenRequestObject;
import CustomerMobileApp.DTO.UserReport;
import io.quarkus.security.UnauthorizedException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

public class CustomerAdapter {
    WebTarget baseUrl;
    Client client;

    public CustomerAdapter() {
        client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/customerapi");
    }

    public String registerCustomer(String firstName, String lastName, String cprNumber, String accountID)
            throws IllegalArgumentException {
        DTUPayUser customer = new DTUPayUser(firstName, lastName, cprNumber, accountID);
        Response response = baseUrl.path("customers").request()
                .post(Entity.entity(customer, MediaType.APPLICATION_JSON));

        if (response.getStatus() == 422) {
            String errorMessage = response.readEntity(String.class); // error message is in payload
            response.close();
            throw new IllegalArgumentException(errorMessage);
        } else if (response.getStatus() >= 300) {
            String errorMessage = response.readEntity(String.class); // error message is in payload
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }

        // TODO: Refactor
        String customerId = response.readEntity(String.class);
        if (customerId.equals("")) {
            throw new RuntimeException("wtf");
        }

        response.close();
        return customerId;
    }

    public List<UUID> createTokensForCustomer(String customerId, int amount) throws Exception {
        TokenRequestObject request = new TokenRequestObject();
        request.setUserId(customerId);
        request.setTokenAmount(amount);
        Response response = baseUrl
                .path("tokens")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        if (response.getStatus() == 401) { // customer is unauthorized (i.e customer has no bank account)
            String errorMessage = response.readEntity(String.class); // error message is in payload
            response.close();
            throw new UnauthorizedException(errorMessage);
        } else if (response.getStatus() == 403) { // Customer not allowed to request more tokens
            String errorMessage = response.readEntity(String.class); // error message is in payload
            response.close();
            throw new Exception(errorMessage);
        }

        List<UUID> createdTokens = response.readEntity(new GenericType<>() {});
        response.close();
        return createdTokens;
    }

    public UserReport getCustomerReport(String customerID) {
        Response response = baseUrl.path("reports").queryParam("id", customerID).request().get(new GenericType<>() {
        });
        if(response.getStatus() == 422){
            String errorMessage = response.readEntity(String.class); // error message is in payload
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }

        UserReport report  = response.readEntity(new GenericType<>() {});
        return report;
    }

    public void close() { client.close(); }
}
