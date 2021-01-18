package CustomerMobileApp.DTO;

import java.math.BigDecimal;
import java.util.*;

public class Payment {

    public UUID customerToken;
    public String merchantId;
    public String description;
    public BigDecimal amount;

    public Payment() {}

    public Payment(UUID customerToken, String merchantId, String description, BigDecimal amount) {
        this.customerToken = customerToken;
        this.merchantId = merchantId;
        this.description = description;
        this.amount = amount;
    }
}