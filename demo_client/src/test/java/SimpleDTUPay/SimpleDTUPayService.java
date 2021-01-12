package SimpleDTUPay;

import SimpleDTUPay.Customer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SimpleDTUPayService {
    WebTarget baseUrl;

    public SimpleDTUPayService() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    public String registerCustomer(String firstName, String lastName, String cprNumber, String accountId) throws IllegalArgumentException {
        Customer customer = new Customer(firstName, lastName, cprNumber, accountId);
        Response response = baseUrl.path("customers").request().post(Entity.entity(customer, MediaType.APPLICATION_JSON));

        if(response.getStatus()==422){
            String errorMessage = response.readEntity(String.class); //error message is in payload
            throw new IllegalArgumentException(errorMessage);
        }
        String customerId = response.readEntity(String.class);
        return customerId;
    }

    public String registerMerchant(String firstName, String lastName, String cprNumber, String merchantAccountId) {
        Customer customer = new Customer(firstName, lastName, cprNumber, merchantAccountId);
        String merchantId = baseUrl.path("merchants").request().post(Entity.entity(customer, MediaType.APPLICATION_JSON),String.class);
        return merchantId;
    }
}
