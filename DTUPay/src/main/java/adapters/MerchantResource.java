package adapters;

import accountservice.AccountServiceFactory;
import accountservice.merchantservice.*;
import DTO.Merchant;
import accounts.IMerchantService;
import accounts.MessageQueueAccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchantapi/merchants")
public class MerchantResource {

    IMerchantService merchantService = new AccountServiceFactory().getService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Merchant merchant) {
        try{
            String merchantId = merchantService.registerMerchant(merchant);
            return Response.ok(merchantId).build();
        } catch (IllegalArgumentException e){
            return Response.status(422).entity(e.getMessage()).build();
        }
    }
}
