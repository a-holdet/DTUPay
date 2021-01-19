package adapters.customeradapters;

import DTO.TokenCreation;
import tokenservice.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/customerapi/tokens")
public class TokenResource {

    ITokenService tokenService = new TokenServiceFactory().getService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTokensForCustomer(TokenCreation request) {

        try {
            List<UUID> tokens = tokenService.createTokens(request);
            return Response.ok(tokens).build();
        } catch (IllegalTokenGrantingException e) {
            return Response.status(403).entity(e.getMessage()).build(); // Forbidden operation (i.e. user tries to request more tokens than allowed"
        } catch (CustomerNotFoundException e) {
            return Response.status(401).entity(e.getMessage()).build(); // 401 = Unauthorized operation (i.e. user with 'cpr' has no bank account.
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Internal server error - sorry :(").build(); // 401 = Unauthorized operation (i.e. user with 'cpr' has no bank account.
        }
    }
}
