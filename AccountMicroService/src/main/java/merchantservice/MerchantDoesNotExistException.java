package merchantservice;

public class MerchantDoesNotExistException extends Exception {
    public MerchantDoesNotExistException(String errormsg) {
        super(errormsg);
    }
}
