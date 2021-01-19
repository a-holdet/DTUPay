package services.token;

public class TokenDoesNotExistException extends Exception {

    public TokenDoesNotExistException(String msg) {
        super(msg);
    }
}
