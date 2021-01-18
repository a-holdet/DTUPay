package CustomerMobileApp.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    public UUID customerToken;
    public String merchantId;
    public String description;
    public BigDecimal amount;
    public String customerId;
    public LocalDateTime datetime;
}
