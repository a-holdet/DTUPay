package adapters.merchantadapters;

import accountservice.AccountServiceFactory;
import DTO.Merchant;
import accountservice.IAccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchantapi/merchants")
public class MerchantResource {

    IAccountService accountService = new AccountServiceFactory().getService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(Merchant merchant) {
        try{
            String merchantId = accountService.registerMerchant(merchant);
            return Response.ok(merchantId).build();
        } catch (IllegalArgumentException e){
            return Response.status(422).entity(e.getMessage()).build();
        }
    }
}
