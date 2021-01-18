package ports;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import java.math.BigDecimal;

public class DTUBankPort implements IBankPort {
    BankService bankService = new BankServiceService().getBankServicePort();

    @Override
    public void transferMoneyFromTo(String customerAccountId, String merchantAccountId, BigDecimal amount, String description) throws BankPortException {
        try {
            bankService.transferMoneyFromTo(
                    customerAccountId,
                    merchantAccountId,
                    amount,
                    description
            );
        } catch (BankServiceException_Exception e) {
            throw new BankPortException("The bank transfer went wrong");
        }
    }
}
