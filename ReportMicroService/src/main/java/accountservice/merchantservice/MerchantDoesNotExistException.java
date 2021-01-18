package accountservice.merchantservice;

public class MerchantDoesNotExistException extends Throwable {
    public MerchantDoesNotExistException(String errormsg) {
        super(errormsg);
    }
}
