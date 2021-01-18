package paymentservice;

import DTO.Payment;
import accountservice.customerservice.CustomerDoesNotExistException;
import accountservice.merchantservice.MerchantDoesNotExistException;
import Bank.BankPortException;
import tokenservice.ConsumeTokenException;
import tokenservice.TokenDoesNotExistException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, ConsumeTokenException, MerchantDoesNotExistException, CustomerDoesNotExistException, NegativeAmountException, BankPortException;
}
