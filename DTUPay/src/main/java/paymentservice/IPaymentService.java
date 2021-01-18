package paymentservice;

import DTO.Payment;
import accountservice.CustomerDoesNotExistException;
import accountservice.MerchantDoesNotExistException;
import tokenservice.TokenDoesNotExistException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, CustomerDoesNotExistException, NegativeAmountException, BankException;
}
