package CustomerMobileApp;

import CustomerMobileApp.DTO.Report;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

public class DTUManagerAdapter {

    WebTarget baseUrl;

    public DTUManagerAdapter() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/managerapi");
    }

    public List<Report> getReport() {
        return baseUrl
                .path("reports")
                .request()
                .get(new GenericType<>() {});
    }
}
