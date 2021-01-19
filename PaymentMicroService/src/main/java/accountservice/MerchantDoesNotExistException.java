package accountservice;

/***
 * @Author Jakob Vestergaard Offersen, s163940
 */

public class MerchantDoesNotExistException extends Exception {
    public MerchantDoesNotExistException(String errormsg) {
        super(errormsg);
    }
}
