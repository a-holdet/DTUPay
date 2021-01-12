package CustomerMobileApp;

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
    class TokenRequestObject {
        String cpr;
        int tokenAmount;

        public TokenRequestObject(String cprNumber, int amount) {
            this.cpr = cprNumber;
            this.tokenAmount = amount;
        }
    }

    WebTarget baseUrl;

    public TokenGenerationAdapter() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    public void createTokensForCustomer(Customer customer, int amount) {
        Response response = baseUrl
                .path("tokens")
                .request()
                .post(Entity.entity(new TokenRequestObject(customer.getCprNumber(), amount), MediaType.APPLICATION_JSON));
    }

    public List<UUID> readTokensForCustomer(Customer customer) {
        return baseUrl
                .path("tokens")
                .request().get(new GenericType<>() {});
    }
}
