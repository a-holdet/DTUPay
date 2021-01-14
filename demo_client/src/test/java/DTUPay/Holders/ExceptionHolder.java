package DTUPay.Holders;

public class ExceptionHolder {

    public static ExceptionHolder instance = new ExceptionHolder();

    public Exception exception;

    private ExceptionHolder() { reset (); }

    public void reset() {
        this.exception = null;
    }
}
