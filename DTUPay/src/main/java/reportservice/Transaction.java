package reportservice;

import DTO.Payment;

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

    public Transaction() {}

    public Transaction(UUID customerToken, String merchantId, String description, BigDecimal amount, String customerId, LocalDateTime datetime) {
        this.customerToken = customerToken;
        this.merchantId = merchantId;
        this.description = description;
        this.amount = amount;
        this.customerId = customerId;
        this.datetime = datetime;
    }

    public Payment toPayment() {
        return new Payment(amount, customerToken, merchantId, description);
    }

    public Transaction(Payment payment, String customerId, LocalDateTime time) {
        this.customerToken = payment.customerToken;
        this.merchantId = payment.merchantId;
        this.description = payment.description;
        this.amount = payment.amount;
        this.customerId = customerId;
        this.datetime = time;
    }
}