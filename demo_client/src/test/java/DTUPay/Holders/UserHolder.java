package DTUPay.Holders;

import java.util.Random;

public abstract class UserHolder {
    private String id;
    private String firstName;
    private String lastName;
    private String accountId;
    private String cpr;

    public void reset(){
        setId(null);
        setFirstName(null);
        setLastName(null);
        setAccountId(null);
        setCpr(null);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    protected int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public String getRandomCpr(){
        int fourRandomDigits = getRandomNumberInRange(1000,9999);
        int anotherFourRandomDigits = getRandomNumberInRange(1000,9999);
        int lastFourRandomDigits = getRandomNumberInRange(1000,9999);
        return fourRandomDigits + anotherFourRandomDigits + "-" + lastFourRandomDigits;
    }
}
