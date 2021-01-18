package Tokens;

import Tokens.TokenDoesNotExistException;

import java.util.UUID;

public interface ITokenService {
    String consumeToken(UUID customerToken) throws TokenDoesNotExistException;
}
