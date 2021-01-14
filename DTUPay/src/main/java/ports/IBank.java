package ports;

import java.math.BigDecimal;

public interface IBank {
    void transferMoneyFromTo(String customerAccountId,
                             String merchantAccountId,
                             BigDecimal amount,
                             String description) throws BankException;
}
