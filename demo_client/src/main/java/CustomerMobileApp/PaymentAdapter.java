package CustomerMobileApp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;

//TODO: make abstract "BaseAdapter" for common initilisation of 'baseURL'
public class PaymentAdapter {

    WebTarget baseUrl;

    public PaymentAdapter() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/");
    }

    public void transferMoneyFromTo(UUID selectedToken, String merchantId, BigDecimal amount, String description) throws IllegalArgumentException {
        Payment payment = new Payment();
        payment.amount = amount;
        payment.customerToken = selectedToken;
        payment.merchantId = merchantId;
        payment.description = description;

        Response response = baseUrl.path("payments").request().post(Entity.entity(payment, MediaType.APPLICATION_JSON));
        if(response.getStatus()<200 || response.getStatus() >= 300){
            String errorMessage = response.readEntity(String.class); //error message is in payload
            response.close();
            throw new IllegalArgumentException(errorMessage);
        }
        response.close();
    }

}
