package SimpleDTUPay;

//TODO: Combine with the 'Customer' in CustomeMobileApp when payments service has been refactored to not rely on accountId.
public class Customer {
    public String firstName;
    public String lastName;
    public String cprNumber;
    public String accountId;

    public Customer(String firstName, String lastName, String cprNumber, String accountId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cprNumber = cprNumber;
        this.accountId = accountId;
    }
}
