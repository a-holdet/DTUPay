package adapters;

import TokenGeneration.TokenGenerationService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/tokens")
public class TokenGenerationResource {

    //TODO: Where should this class live?
    public static class TokenRequestObject {
        private String cpr;
        private int tokenAmount;

        public TokenRequestObject() {}

        public void setCpr(String cpr) {
            this.cpr = cpr;
        }

        public void setTokenAmount(int tokenAmount) {
            this.tokenAmount = tokenAmount;
        }

        public String getCpr() {
            return cpr;
        }

        public int getTokenAmount() {
            return tokenAmount;
        }
    }

    TokenGenerationService tokenGenerationService = TokenGenerationService.instance;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTokensForCustomer(TokenRequestObject request) {
        String cpr = request.getCpr();
        int amount = request.getTokenAmount();
        tokenGenerationService.createTokensForCustomer(cpr, amount);
        return Response.ok().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readTokensForCustomer(@QueryParam("cpr") String cpr) {
        List<UUID> tokens = tokenGenerationService.readTokensForCustomer(cpr);

        return Response.ok(tokens).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTokensForCustomer(@QueryParam("cpr") String cpr) {
        tokenGenerationService.deleteTokensForCustomer(cpr);

        return Response.ok().build();
    }
}
