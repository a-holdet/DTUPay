package merchantservice;

public class MerchantDoesNotExistException extends Throwable {
    public MerchantDoesNotExistException(String merchantId) {
        super("The merchant '" + merchantId + "' does not exist in DTUPay");
    }
}
