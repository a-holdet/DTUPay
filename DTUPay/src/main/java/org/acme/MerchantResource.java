package org.acme;

import BusinessLogic.CustomerService;
import BusinessLogic.Merchant;
import BusinessLogic.MerchantService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchants")
public class MerchantResource {
    MerchantService merchantService = MerchantService.instance;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String create(Merchant merchant) {
        merchantService.registerMerchant(merchant);
        return merchant.id;
    }
}
