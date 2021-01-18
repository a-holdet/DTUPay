package paymentservice;

import DTO.Payment;
import accounts.CustomerDoesNotExistException;
import accounts.MerchantDoesNotExistException;
import tokenservice.TokenDoesNotExistException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, CustomerDoesNotExistException, NegativeAmountException, BankException;
}
