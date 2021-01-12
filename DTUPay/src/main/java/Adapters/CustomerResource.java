package org.acme;

import BusinessLogic.Customer;
import BusinessLogic.CustomerService;
import BusinessLogic.Payment;
import BusinessLogic.PaymentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers")
public class CustomerResource {
    CustomerService customerService = CustomerService.instance;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Customer customer) {
        try{
            String customerId = customerService.registerCustomer(customer);
            return Response.ok(customerId).build();
        }catch(IllegalArgumentException e){
            return Response.status(422).entity(e.getMessage()).build();
        }
    }
}
