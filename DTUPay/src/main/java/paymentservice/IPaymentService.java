package paymentservice;

import ports.BankException;
import tokenservice.TokenDoesNotExistException;

import java.util.List;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException, BankException;
}
