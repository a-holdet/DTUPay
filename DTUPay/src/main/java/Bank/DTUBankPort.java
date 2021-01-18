package Bank;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

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
            System.out.println("what went wrong?" + e.getMessage());
            throw new BankException("The bank transfer went wrong");
        }
    }
}
