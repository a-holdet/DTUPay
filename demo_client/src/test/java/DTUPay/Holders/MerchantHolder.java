package DTUPay.Holders;

public class MerchantHolder extends UserHolder{
    public void setMerchantBasics(){
        int fourRandomDigits = getRandomNumberInRange(1000,9999);
        String cpr = "1503634321-"+ fourRandomDigits;

        setFirstName("Joe");
        setLastName("Exotic");
        setCpr(cpr);
    }
}
