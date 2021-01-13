package adapters;

import eventservice.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/status")
public class Service2Resource {

	IEventService eventService = RabbitMQEventService.instance;

	@PUT
	@Consumes("text/plain")
	public boolean doSomething(String status) {
		try {
			return eventService.doSomething();
		} catch (Exception e) {
			throw new Error(e);
		}
	}
}
