package Bank;


import fastmoney.BankService;
import fastmoney.BankServiceException_Exception;
import fastmoney.BankServiceService;

import java.math.BigDecimal;
public class DTUBankPort implements IBank{
    BankService bankService = new BankServiceService().getBankServicePort();

    @Override
    public void transferMoneyFromTo(String customerAccountId, String merchantAccountId, BigDecimal amount, String description) throws BankException {
        try {
            bankService.transferMoneyFromTo(
                    customerAccountId,
                    merchantAccountId,
                    amount,
                    description
            );
        } catch (BankServiceException_Exception e) {
            throw new BankException("The bank transfer went wrong");
        }
    }
}
