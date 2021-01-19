package bank;

import java.math.BigDecimal;
/***
 * @Author Martin Hemmingsen, s141887
 */


public interface IBank {
    void transferMoneyFromTo(String customerAccountId, String merchantAccountId, BigDecimal amount, String description) throws BankException;
}
