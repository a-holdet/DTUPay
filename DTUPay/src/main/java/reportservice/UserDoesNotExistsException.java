package reportservice;

public class UserDoesNotExistsException extends Throwable {
    public UserDoesNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
