package DTUPay.Holders;

public class UserHolder {
    public String id;
    public String firstName;
    public String lastName;
    public String accountId;
    public String cpr;

    public static UserHolder merchant = new UserHolder();
    public static UserHolder customer = new UserHolder();

    public void reset(){
        id = null;
        firstName = null;
        lastName = null;
        accountId = null;
        cpr = null;
    }
}
