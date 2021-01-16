package tokenservice;

import io.quarkus.security.UnauthorizedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/customerapi/tokens")
public class TokenResource {
    ITokenService tokenService  = TokenService.instance;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTokensForCustomer(TokenCreationDTO request) {
        String customerId = request.getUserId();
        int amount = request.getTokenAmount();
        try {
            List<UUID> tokens = tokenService.createTokensForCustomer(customerId, amount);
            return Response.ok(tokens).build();
        } catch (UnauthorizedException e) {
            System.out.println("unauth" + e.getMessage());
            return Response.status(401).entity(e.getMessage()).build(); // 401 = Unauthorized operation (i.e. user with 'cpr' has no bank account.
        } catch (IllegalTokenGrantingException e) {
            System.out.println("IllegalTokenGrantingException" + e.getMessage());
            return Response.status(403).entity(e.getMessage()).build(); // Forbidden operation (i.e. user tries to request more tokens than allowed"
        }
    }
}
