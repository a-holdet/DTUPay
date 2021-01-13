package adapters;

import TokenGeneration.ITokenService;
import TokenGeneration.TokenGenerationService;
import TokenGeneration.TokenService;
import io.quarkus.security.UnauthorizedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/tokens")
public class TokenResource {

    //TODO: Where should this class live?
    public static class TokenRequestObject {
        private String userId;
        private int tokenAmount;

        public TokenRequestObject() {}

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setTokenAmount(int tokenAmount) {
            this.tokenAmount = tokenAmount;
        }

        public String getUserId() {
            return userId;
        }

        public int getTokenAmount() {
            return tokenAmount;
        }
    }

    ITokenService tokenService  = TokenService.instance;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTokensForCustomer(TokenRequestObject request) {
        String customerId = request.getUserId();
        int amount = request.getTokenAmount();
        try {
            List<UUID> tokens = tokenService.createTokensForCustomer(customerId, amount);
            return Response.ok(tokens).build();
        } catch (UnauthorizedException e) {
            return Response.status(401).entity(e.getMessage()).build(); // 401 = Unauthorized operation (i.e. user with 'cpr' has no bank account.
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response readTokensForCustomer(@QueryParam("id") String customerId) {
        List<UUID> tokens = tokenService.readTokensForCustomer(customerId);
        return Response.ok(tokens).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTokensForCustomer(@QueryParam("cpr") String cpr) {
        tokenService.deleteTokensForCustomer(cpr);

        return Response.ok().build();
    }
}
