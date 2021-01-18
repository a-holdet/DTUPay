package CustomerMobileApp;

import CustomerMobileApp.DTO.Transaction;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

public class DTUManagerAdapter {

    Client client;
    WebTarget baseUrl;

    public DTUManagerAdapter() {
        client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/managerapi");
    }

    public List<Transaction> getManagerOverview() {
        return baseUrl.path("reports").request().get(new GenericType<>() {
        });
    }

    public void close() {  
        client.close();
    }
}
