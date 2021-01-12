package org.acme;

import BusinessLogic.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/payments")
public class PaymentsResource {
    PaymentService paymentService = PaymentService.instance;

    public PaymentsResource(){
        System.out.println("abc");
    }

    @POST
    //@Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Payment payment) {
        try {
            paymentService.registerPayment(payment);
        } catch (MerchantDoesNotExistException e) {
            throw new NotFoundException();
        } catch (CustomerDoesNotExistException e) {
            System.out.println("resource" + e.getMessage());
            throw new NotFoundException(e.getMessage());
        }
        return Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Payment> list() {
        return paymentService.getPayments();
    }
}