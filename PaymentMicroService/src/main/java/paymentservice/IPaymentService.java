package paymentservice;

import accountservice.CustomerDoesNotExistException;
import accountservice.MerchantDoesNotExistException;
import bank.BankException;
import tokenservice.TokenDoesNotExistException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, CustomerDoesNotExistException, NegativeAmountException, BankException;
}
