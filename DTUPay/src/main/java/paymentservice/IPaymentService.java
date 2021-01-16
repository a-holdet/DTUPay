package paymentservice;

import DTO.Payment;
import customerservice.CustomerDoesNotExcistException;
import merchantservice.MerchantDoesNotExistException;
import ports.BankException;
import tokenservice.TokenDoesNotExistException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, CustomerDoesNotExcistException, NegativeAmountException, BankException;
}
