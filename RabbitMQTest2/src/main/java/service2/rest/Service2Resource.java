package service2.rest;


import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import service2.businesslogic.Service2;

@Path("/status")
public class Service2Resource {
	
	@PUT
	@Consumes("text/plain")
	public boolean doSomething(String status) {
		Service2 service = new Service2Factory().getService();
		try {
			return service.doSomething();
		} catch (Exception e) {
			throw new Error(e);
		}
	}
}
