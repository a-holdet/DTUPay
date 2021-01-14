package CustomerMobileApp;

import dtu.ws.fastmoney.User;
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

public class TokenGenerationAdapter {

    //TODO: Is this the right way to post?
    //TODO: And where should this class live?
    //TODO: Where should this class live?
    public static class TokenRequestObject {
        private String userId;
        private int tokenAmount;

        public TokenRequestObject() {
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setTokenAmount(int tokenAmount) {
            this.tokenAmount = tokenAmount;
        }

        public String getUserId() {
            return userId;
        }

        public int getTokenAmount() {
            return tokenAmount;
        }
    }

    WebTarget baseUrl;

    public TokenGenerationAdapter() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/");
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
            throw new UnauthorizedException(errorMessage);
        } else if (response.getStatus() == 403) { // Customer not allowed to request more tokens
            String errorMessage = response.readEntity(String.class); // error message is in payload
            throw new Exception(errorMessage);
        }

        return response.readEntity(new GenericType<>() {
        });
    }

    public List<UUID> readTokensForCustomer(String customerId) {
        return baseUrl
                .path("tokens")
                .queryParam("id", customerId)
                .request().get(new GenericType<>() {
                });
    }

    public void deleteTokensFor(String customerId) {
        baseUrl.path("tokens")
                .queryParam("id", customerId)
                .request()
                .delete();
    }
}
