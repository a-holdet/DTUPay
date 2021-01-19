package bank;

/***
 * @Author Martin Hemmingsen, s141887
 */

public class BankException extends Exception {
    public BankException(String errorMessage) {
        super(errorMessage);
    }
}
