package DTUPay;

public class UserHolder {
    public String id;
    public String firstName;
    public String lastName;
    public String accountId;
    public String cpr;

    public static UserHolder merchant = new UserHolder();
    public static UserHolder customer = new UserHolder();

    public static void reset(){
        merchant = new UserHolder();
        customer = new UserHolder();
    }
}
