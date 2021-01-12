package DTUPay;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

public class DTUPayService {
    WebTarget baseUrl;

    public DTUPayService() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/");
    }

    public String registerCustomer(String firstName, String lastName, String cprNumber, String accountId) throws IllegalArgumentException {
        RestUser restUser = new RestUser();
        restUser.firstName=firstName;
        restUser.lastName=lastName;
        restUser.cprNumber =cprNumber;
        restUser.accountId=accountId;

        Response response = baseUrl.path("customers").request().post(Entity.entity(restUser, MediaType.APPLICATION_JSON));

        if(response.getStatus()==422){
            String errorMessage = response.readEntity(String.class); //error message is in payload
            throw new IllegalArgumentException(errorMessage);
        }
        String customerId = response.readEntity(String.class);
        return customerId;
    }

    public String registerMerchant(String firstName, String lastName, String cprNumber, String merchantAccountId) {
        RestUser restUser = new RestUser();
        restUser.firstName=firstName;
        restUser.lastName=lastName;
        restUser.cprNumber =cprNumber;
        restUser.accountId=merchantAccountId;
        String merchantId = baseUrl.path("merchants").request().post(Entity.entity(restUser, MediaType.APPLICATION_JSON),String.class);
        return merchantId;
    }

    public void transferMoneyFromTo(String customerAccountId, String merchantAccountId, BigDecimal amount, String description) throws Exception {
        Payment payment = new Payment();
        payment.amount = amount;
        payment.customerAccountId = customerAccountId;
        payment.merchantAccountId = merchantAccountId;
        payment.description = description;

        Response response = baseUrl.path("payments").request().post(Entity.entity(payment, MediaType.APPLICATION_JSON));
        if(response.getStatus()<200 || response.getStatus() >= 300){
            throw new Exception("Something went wong");
        }
    }

}
