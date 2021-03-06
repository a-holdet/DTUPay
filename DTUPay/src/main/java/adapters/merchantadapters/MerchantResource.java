package adapters.merchantadapters;

import services.accountservice.AccountServiceFactory;
import DTO.Merchant;
import services.accountservice.IAccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/***
 * @Author Jakob Vestergaard Offersen, s163940
 */


@Path("/merchantapi/merchants")
public class MerchantResource {

    IAccountService accountService = new AccountServiceFactory().getService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Merchant merchant) {
        try{
            String merchantId = accountService.registerMerchant(merchant);
            return Response.ok(merchantId).build();
        } catch (IllegalArgumentException e){
            return Response.status(422).entity(e.getMessage()).build();
        }
    }
}
