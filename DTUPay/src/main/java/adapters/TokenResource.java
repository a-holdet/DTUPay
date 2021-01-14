package adapters;

import tokenservice.ITokenService;
import tokenservice.IllegalTokenGrantingException;
import tokenservice.TokenCreationDTO;
import tokenservice.TokenService;
import io.quarkus.security.UnauthorizedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/tokens")
public class TokenResource {
    ITokenService tokenService  = TokenService.instance;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTokensForCustomer(TokenCreationDTO request) {
        String customerId = request.getUserId();
        int amount = request.getTokenAmount();
        try {
            List<UUID> tokens = tokenService.createTokensForCustomer(customerId, amount);
            return Response.ok(tokens).build();
        } catch (UnauthorizedException e) {
            return Response.status(401).entity(e.getMessage()).build(); // 401 = Unauthorized operation (i.e. user with 'cpr' has no bank account.
        } catch (IllegalTokenGrantingException e) {
            return Response.status(403).entity(e.getMessage()).build(); // Forbidden operation (i.e. user tries to request more tokens than allowed"
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
