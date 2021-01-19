package DTUPay.Holders;

public class MerchantHolder extends UserHolder{
    public void setMerchantBasics(){
        String cpr = getRandomCpr();
        setFirstName("Mickey");
        setLastName("Mouse");
        setCpr(cpr);
    }
}
