package DTUPay.Holders;

public class CustomerHolder extends UserHolder{
    public void setCustomerBasics(){
        String cpr = getRandomCpr();
        setFirstName("Stein");
        setLastName("Bagger");
        setCpr(cpr);
    }
}
