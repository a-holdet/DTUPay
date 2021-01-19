package DTUPay.Holders;

public class MerchantHolder extends UserHolder{
    public void setMerchantBasics(){
        String cpr = getRandomCpr();
        setFirstName("Helle");
        setLastName("T");
        setCpr(cpr);
    }
}
