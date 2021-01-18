package customerservice;

public class Customer {
    public String firstName;
    public String lastName;
    public String cprNumber;
    public String accountId;
    public String id;

    public boolean hasValidAccountId() {
        return accountId != null && accountId.length() > 0;
    }
}
