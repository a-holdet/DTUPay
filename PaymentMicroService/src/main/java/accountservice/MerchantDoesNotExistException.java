package accountservice;

public class MerchantDoesNotExistException extends Exception {
    public MerchantDoesNotExistException(String errormsg) {
        super(errormsg);
    }
}
