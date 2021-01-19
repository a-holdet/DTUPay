package services.token;

import DTO.TokenCreationDTO;
import java.util.List;
import java.util.UUID;

public interface ITokenService {
    List<UUID> createTokens(TokenCreationDTO tokenCreationDTO) throws IllegalTokenGrantingException, Exception;
}
