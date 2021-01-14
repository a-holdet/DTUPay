package paymentservice;

import java.math.BigDecimal;
import java.util.UUID;

public class Payment {
    public BigDecimal amount;
    public UUID customerToken;
    public String merchantId;
    public String description;
    public Payment(){}
}
