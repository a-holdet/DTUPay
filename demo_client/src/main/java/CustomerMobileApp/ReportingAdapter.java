package CustomerMobileApp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

public class ReportingAdapter {

    WebTarget baseUrl;

    public ReportingAdapter() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8042/");
    }

    public Report getReportFor(String merchantId) {
        return baseUrl
                .path("reports")
                .queryParam("id", merchantId)
                .request()
                .get(new GenericType<>() {});
    }
}
