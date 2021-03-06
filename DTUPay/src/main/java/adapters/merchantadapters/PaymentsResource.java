package adapters.merchantadapters;

import DTO.Payment;
import services.accountservice.CustomerDoesNotExistException;
import services.accountservice.MerchantDoesNotExistException;
import services.paymentservice.BankException;
import services.paymentservice.IPaymentService;
import services.paymentservice.NegativeAmountException;
import services.paymentservice.PaymentServiceFactory;
import services.tokenservice.TokenDoesNotExistException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/***
 * @Author Benjamin Wrist Lam, s153486
 */

@Path("/merchantapi/payments")
public class PaymentsResource {
    IPaymentService paymentService = new PaymentServiceFactory().getService();

    @POST
    //@Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Payment payment) {
        // Send Payment Out

        System.out.println("PAYMENTS RESOURCE:");
        try {
            paymentService.registerPayment(payment);
        } catch (BankException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (TokenDoesNotExistException | MerchantDoesNotExistException | CustomerDoesNotExistException | NegativeAmountException e) {
            return Response.status(422).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Internal server error").build();
        }
        return Response.noContent().build();
    }
}