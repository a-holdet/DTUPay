package Bank;

import java.math.BigDecimal;

public interface IBankPort {
    void transferMoneyFromTo(String customerAccountId,
                             String merchantAccountId,
                             BigDecimal amount,
                             String description) throws BankPortException;
}
