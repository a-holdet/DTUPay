package DTUPay.Holders;

public class CustomerHolder extends UserHolder{
    public void setCustomerBasics(){ //TODO move to holder
        int fourRandomDigits = getRandomNumberInRange(1000,9999);
        String cpr = "200167-"+ fourRandomDigits;

        setFirstName("Stein");
        setLastName("Bagger");
        setCpr(cpr);
    }
}
