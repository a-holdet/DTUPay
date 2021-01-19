package services.paymentservice;

import DTO.Payment;
import services.accountservice.CustomerDoesNotExistException;
import services.accountservice.MerchantDoesNotExistException;
import services.tokenservice.TokenDoesNotExistException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, CustomerDoesNotExistException, NegativeAmountException, BankException;
}
