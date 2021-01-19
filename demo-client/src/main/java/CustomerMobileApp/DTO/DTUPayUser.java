package CustomerMobileApp.DTO;

public class DTUPayUser {
    private String firstName;
    private String lastName;
    private String cprNumber;
    private String accountId;

    public DTUPayUser(){

    }

    public DTUPayUser(String firstName, String lastName, String cprNumber, String accountId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cprNumber = cprNumber;
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCprNumber() {
        return cprNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
