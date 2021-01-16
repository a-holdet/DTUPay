package adapters;

import merchantservice.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*@Path("host1/customerapi/customer/{ìd}/payments")
@Path("host1/merchantapi/mearchants/{ìd}/payments")
OR
@Path("<merchanthost>/mearchants/{ìd}/payments")
@Path("<customerhost>/customers/{ìd}/payments")*/

@Path("/merchantapi/merchants")
public class MerchantResource {
    IMerchantService merchantService = LocalMerchantService.instance;

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
