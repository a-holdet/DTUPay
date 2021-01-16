package merchantservice;

//TODO: Refactor to DTUPayUser instead?
public class Merchant {
    public String firstName;
    public String lastName;
    public String cprNumber;
    public String accountId;
    public String id;

    public Merchant(){};

    public Merchant(String firstName, String lastName, String cprNumber, String accountId, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cprNumber = cprNumber;
        this.accountId = accountId;
        this.id = id;
    }
}
