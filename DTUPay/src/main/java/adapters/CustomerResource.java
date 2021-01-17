package adapters;

import customerservice.*;
import merchantservice.IMerchantService;
import merchantservice.MessageQueueAccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customerapi/customers")
public class CustomerResource {

    ICustomerService customerService = MessageQueueAccountService.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Customer customer) {
        try{
            customerService.registerCustomer(customer);
            String customerId = customerService.registerCustomer(customer);
            return Response.ok(customerId).build();
        }catch(IllegalArgumentException e){
            return Response.status(422).entity(e.getMessage()).build();
        }
    }

}

