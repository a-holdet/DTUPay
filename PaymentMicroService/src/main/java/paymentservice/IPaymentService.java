package paymentservice;

import Accounts.CustomerDoesNotExistException;
import Accounts.MerchantDoesNotExistException;
import Bank.BankException;
import DTO.Payment;
import Tokens.TokenDoesNotExistException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, CustomerDoesNotExistException, NegativeAmountException, BankException;
}
