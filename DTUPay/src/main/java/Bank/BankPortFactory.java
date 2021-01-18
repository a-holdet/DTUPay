package Bank;

public class BankPortFactory {

    static IBankPort port;
    public IBankPort getPort() {
        if(port == null) {
            port = new DTUBankPort();
        }
        return port;
    }
}
