package CustomerMobileApp;

import io.cucumber.gherkin.Token;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

public class TokenGenerationAdapter implements ITokenGeneration {

    //TODO: Is this the right way to post?
    //TODO: And where should this class live?
    //TODO: Where should this class live?
    public static class TokenRequestObject {
        private String cpr;
        private int tokenAmount;

        public TokenRequestObject() {}

        public void setCpr(String cpr) {
            this.cpr = cpr;
        }

        public void setTokenAmount(int tokenAmount) {
            this.tokenAmount = tokenAmount;
        }

        public String getCpr() {
            return cpr;
        }

        public int getTokenAmount() {
            return tokenAmount;
        }
    }

    WebTarget baseUrl;

    public TokenGenerationAdapter() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    public void createTokensForCustomer(Customer customer, int amount) {
        TokenRequestObject request = new TokenRequestObject();
        request.setCpr(customer.getCprNumber());
        request.setTokenAmount(amount);
        Response response = baseUrl
                .path("tokens")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));
        //TODO: Should we do something with status code of response here?
    }

    public List<UUID> readTokensForCustomer(Customer customer) {
        return baseUrl
                .path("tokens")
                .queryParam("cpr", customer.getCprNumber())
                .request().get(new GenericType<>() {});
    }

    @Override
    public void deleteTokensFor(Customer customer) {
        baseUrl.path("tokens")
                .queryParam("cpr", customer.getCprNumber())
                .request()
                .delete();
    }
}
