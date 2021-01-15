package DTUPay.Holders;

public class MerchantHolder extends UserHolder{
    public void setMerchantBasics(){
        String cpr = getRandomCpr();
        setFirstName("Joe");
        setLastName("Exotic");
        setCpr(cpr);
    }
}
