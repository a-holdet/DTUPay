package services.payment;

import DTO.Payment;
import services.accountservice.CustomerDoesNotExistException;
import services.accountservice.MerchantDoesNotExistException;
import services.token.TokenDoesNotExistException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, CustomerDoesNotExistException, NegativeAmountException, BankException;
}
