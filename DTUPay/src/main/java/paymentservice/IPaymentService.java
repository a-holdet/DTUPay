package paymentservice;

import Bank.BankException;
import DTO.Payment;
import customerservice.CustomerDoesNotExistException;
import merchantservice.MerchantDoesNotExistException;
import tokenservice.ConsumeTokenException;
import tokenservice.TokenDoesNotExistException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, ConsumeTokenException, MerchantDoesNotExistException, CustomerDoesNotExistException, NegativeAmountException, BankException;
}
