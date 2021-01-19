package adapters.merchantadapters;

import DTO.Payment;
import accountservice.CustomerDoesNotExistException;
import accountservice.MerchantDoesNotExistException;
import paymentservice.*;
import tokenservice.TokenDoesNotExistException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;

@Path("/merchantapi/merchants/{id}/payments")
public class PaymentsResource {
    IPaymentService paymentService = new PaymentServiceFactory().getService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@PathParam("id") String merchantId, PaymentInput paymentInput) {

        System.out.println(paymentInput.amount);
        System.out.println(paymentInput.customerToken);
        System.out.println(merchantId);
        System.out.println(paymentInput.description);

        Payment payment = new Payment(paymentInput.amount, paymentInput.customerToken, merchantId, paymentInput.description);

        System.out.println(payment.amount);
        System.out.println(payment.customerToken);
        System.out.println(payment.merchantId);
        System.out.println(payment.description);

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