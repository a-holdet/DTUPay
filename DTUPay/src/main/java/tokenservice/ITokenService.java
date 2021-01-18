package tokenservice;

import DTO.TokenCreation;
import java.util.List;
import java.util.UUID;

public interface ITokenService {
    List<UUID> createTokens(TokenCreation tokenCreation) throws IllegalTokenGrantingException, Exception;
}
