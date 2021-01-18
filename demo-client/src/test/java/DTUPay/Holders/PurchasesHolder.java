package DTUPay.Holders;

import java.util.ArrayList;
import java.util.List;

public class PurchasesHolder {

    public static class Purchase {
        public int amount;
        public String description;

        public Purchase(int amount, String description) {
            this.amount = amount;
            this.description = description;
        }
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    private List<Purchase> purchases = new ArrayList<>();

    public PurchasesHolder() {
        reset();
    }

    public void reset() { this.purchases = new ArrayList<>(); }

    public void add(int amount, String description) {
        purchases.add(new Purchase(amount, description));
    }
}
