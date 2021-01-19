package tokenservice;

public class IllegalTokenGrantingException extends Exception {
    public IllegalTokenGrantingException(String message) {
        super(message);
    }
}