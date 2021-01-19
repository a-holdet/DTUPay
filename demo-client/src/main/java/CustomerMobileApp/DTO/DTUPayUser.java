package CustomerMobileApp.DTO;

public class DTUPayUser {
    private final String firstName;
    private final String lastName;
    private final String cprNumber;

    public DTUPayUser(String firstName, String lastName, String cprNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cprNumber = cprNumber;
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

}
