package CustomerMobileApp;

public class Customer {
    private String firstName;
    private String lastName;
    private String cprNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCprNumber() {
        return cprNumber;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    private String accountId;

    public Customer(String firstName, String lastName, String cprNumber, String accountId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cprNumber = cprNumber;
        this.accountId = accountId;
    }
}
