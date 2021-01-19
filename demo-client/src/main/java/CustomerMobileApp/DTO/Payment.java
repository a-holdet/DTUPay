package CustomerMobileApp.DTO;

import java.math.BigDecimal;
import java.util.*;

public class Payment {
    private UUID customerToken;
    private String merchantId;
    private String description;
    private BigDecimal amount;

    public Payment(){

    }
    public Payment(UUID customerToken, String merchantId, String description, BigDecimal amount) {
        this.customerToken = customerToken;
        this.merchantId = merchantId;
        this.description = description;
        this.amount = amount;
    }

    public UUID getCustomerToken() {
        return customerToken;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setCustomerToken(UUID customerToken) {
        this.customerToken = customerToken;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}