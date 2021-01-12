package BusinessLogic;

public class Payment {
    public int amount;
    public String customerId;
    public String merchantId;

    public Payment(){}

    public Payment(int amount, String customerId, String merchantId){
        this.amount=amount;
        this.customerId=customerId;
        this.merchantId=merchantId;
    }
}
