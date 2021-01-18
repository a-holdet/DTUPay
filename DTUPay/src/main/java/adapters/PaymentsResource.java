package adapters;

import DTO.Payment;
import accountservice.customerservice.CustomerDoesNotExistException;
import accountservice.merchantservice.MerchantDoesNotExistException;
import paymentservice.*;
import Bank.BankPortException;
import tokenservice.ConsumeTokenException;
import tokenservice.TokenDoesNotExistException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        } catch (BankPortException e) {
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