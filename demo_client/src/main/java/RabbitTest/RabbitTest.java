package RabbitTest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

public class RabbitTest {

	public boolean doSomething() {
		Client client = ClientBuilder.newClient();
	    WebTarget r = client.target("http://localhost:8042/");
		boolean response = r.path("status").request().put(Entity.text("doSomething"),Boolean.class);
		client.close();
	    return response;
	}
}
