package adapters;

import DTO.Payment;
import customerservice.CustomerDoesNotExistException;
import merchantservice.MerchantDoesNotExistException;
import paymentservice.*;
import ports.BankException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchantapi/payments")
public class PaymentsResource {
    IPaymentService paymentService = PaymentService.getInstance();


    public PaymentsResource(){
        System.out.println("this is run");
        PaymentPortAdapter.startUp();
        paymentService = PaymentService.getInstance();
    }

    // -- HER //
    @POST
    //@Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Payment payment) {
        // Send Payment Out

        try {
            paymentService.registerPayment(payment);
        } catch (BankException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (TokenDoesNotExistException | MerchantDoesNotExistException | CustomerDoesNotExistException | NegativeAmountException e) {
            return Response.status(422).entity(e.getMessage()).build();
        }
        return Response.noContent().build();
    }
}