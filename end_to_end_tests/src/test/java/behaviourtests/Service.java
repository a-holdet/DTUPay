package behaviourtests;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class Service {

	public boolean doSomething() {
		Client client = ClientBuilder.newClient();
	    WebTarget r = client.target("http://localhost:8080/");
	    var response = r.path("status").request().put(Entity.text("doSomething"),Boolean.class);
	    return response;
	}
}
