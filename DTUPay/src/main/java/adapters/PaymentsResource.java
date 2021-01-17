package adapters;

import DTO.Payment;
import customerservice.CustomerDoesNotExistException;
import merchantservice.MerchantDoesNotExistException;
import paymentservice.*;
import ports.BankException;
import tokenservice.ConsumeTokenException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchantapi/payments")
public class PaymentsResource {
    IPaymentService paymentService = PaymentService.getInstance();


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
        }catch (ConsumeTokenException e) {
            return Response.status(403).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Internal server error").build();
        }
        return Response.noContent().build();
    }
}