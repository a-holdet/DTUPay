package accounts;

public class MerchantDoesNotExistException extends Throwable {
    public MerchantDoesNotExistException(String errormsg) {
        super(errormsg);
    }
}
