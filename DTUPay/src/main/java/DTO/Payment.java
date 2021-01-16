package DTO;

import java.math.BigDecimal;
import java.util.UUID;

public class Payment {
    public BigDecimal amount;
    public UUID customerToken;
    public String merchantId;
    public String description;

    public Payment(BigDecimal amount, UUID customerToken, String merchantId, String description) {
        this.amount = amount;
        this.customerToken = customerToken;
        this.merchantId = merchantId;
        this.description = description;
    }

    public Payment() {}
}
