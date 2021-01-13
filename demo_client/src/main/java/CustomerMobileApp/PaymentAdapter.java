package CustomerMobileApp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

//TODO: make abstract "BaseAdapter" for common initilisation of 'baseURL'
public class PaymentAdapter {

    WebTarget baseUrl;

    public PaymentAdapter() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/");
    }

    public void transferMoneyFromTo(String customerAccountId, String merchantId, BigDecimal amount, String description) throws Exception {
        Payment payment = new Payment();
        payment.amount = amount;
        payment.customerAccountId = customerAccountId;
        payment.merchantId = merchantId;
        payment.description = description;

        Response response = baseUrl.path("payments").request().post(Entity.entity(payment, MediaType.APPLICATION_JSON));
        if(response.getStatus()<200 || response.getStatus() >= 300){
            throw new Exception("Something went wong");
        }
    }

}
