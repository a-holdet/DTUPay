package adapters;

import dtu.ws.fastmoney.BankServiceException_Exception;
import paymentservice.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/payments")
public class PaymentsResource {
    IPaymentService paymentService = PaymentService.instance;

    public PaymentsResource(){
    }

    @POST
    //@Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Payment payment) {
        try {
            paymentService.registerPayment(payment);
        /*} catch (MerchantDoesNotExistException e) {
            throw new NotFoundException(e.getMessage());
        } catch (CustomerDoesNotExistException e) {
            throw new BadRequestException(e.getMessage());*/
        } catch (BankServiceException_Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        return Response.noContent().build();

    }
}