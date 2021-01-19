package bank;


import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import java.math.BigDecimal;
/***
 * @Author Benjamin Wrist Lam, s153486
 */



public class DTUBankPort implements IBank {

    private BankService bankService = new BankServiceService().getBankServicePort();

    @Override
    public void transferMoneyFromTo(String customerAccountId, String merchantAccountId, BigDecimal amount, String description) throws BankException {
        try {
            bankService.transferMoneyFromTo(customerAccountId, merchantAccountId, amount, description);
        } catch (BankServiceException_Exception e) {
            throw new BankException("The bank transfer went wrong");
        }
    }
}