package CustomerMobileApp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UserManagementAdapter {

    WebTarget baseUrl;

    public UserManagementAdapter() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/");
    }

    public String registerCustomer(String firstName, String lastName, String cprNumber, String accountID) throws IllegalArgumentException {
        DTUPayUser customer = new DTUPayUser(firstName, lastName, cprNumber, accountID);
        Response response = baseUrl.path("customers").request().post(Entity.entity(customer, MediaType.APPLICATION_JSON));

        if(response.getStatus()==422){
            String errorMessage = response.readEntity(String.class); //error message is in payload
            throw new IllegalArgumentException(errorMessage);
        }

        //TODO: unsure what is returned by server here.
        // Assumes it to be accountId.
        String accountId = response.readEntity(String.class);
        return accountId;
    }

    public String registerMerchant(String firstName, String lastName, String cprNumber, String merchantAccountId) {
        DTUPayUser customer = new DTUPayUser(firstName, lastName, cprNumber, merchantAccountId);
        String merchantId = baseUrl.path("merchants").request().post(Entity.entity(customer, MediaType.APPLICATION_JSON),String.class);
        return merchantId;
    }
}
