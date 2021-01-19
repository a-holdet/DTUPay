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

public class CustomerPort {
    WebTarget baseUrl;
    Client client;

    public CustomerPort() {
        client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/customerapi");
    }

    public String registerCustomer(String firstName, String lastName, String cprNumber, String accountId)
            throws IllegalArgumentException {
        DTUPayUser customer = new DTUPayUser(firstName, lastName, cprNumber, accountId);
        Response response = baseUrl.path("customers").request()
                .post(Entity.entity(customer, MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
        if (response.getStatus() == 422) {
            String errorMessage = response.readEntity(String.class); // error message is in payload
            System.out.println(errorMessage);
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }

        String customerId = response.readEntity(String.class);

        response.close();
        return customerId;
    }

    public List<UUID> createTokensForCustomer(String customerId, int tokenAmount) throws Exception {

        Object inputRequest = new Object() {
            Integer amount = tokenAmount;
        };

        Response response = baseUrl
                .path("customers").path(customerId).path("tokens")
                .request()
                .post(Entity.entity(inputRequest, MediaType.APPLICATION_JSON));

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
        Response response = baseUrl
                .path("customers").path(customerID).path("reports")
                .request().get(new GenericType<>() {
        });
        if (response.getStatus() == 422) {
            String errorMessage = response.readEntity(String.class); // error message is in payload
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }

        return response.readEntity(new GenericType<>() {});
    }

    public void close() {
        client.close();
    }
}
