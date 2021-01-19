package adapters.merchantadapters;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentInput {
    public BigDecimal amount;
    public UUID customerToken;
    public String description;
}