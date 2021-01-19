package adapters.customeradapters;

import DTO.Customer;
import accountservice.AccountServiceFactory;
import accountservice.IAccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customerapi/customers/")
public class CustomerResource {

    IAccountService accountService = new AccountServiceFactory().getService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(Customer customer) {
        try{
            String customerId = accountService.registerCustomer(customer);
            return Response.ok(customerId).build();
        }catch(IllegalArgumentException e){
            return Response.status(422).entity(e.getMessage()).build();
        }
    }

}

