package paymentservice;

import DTO.Payment;
import customerservice.CustomerDoesNotExistException;
import merchantservice.MerchantDoesNotExistException;
import ports.BankException;
import tokenservice.ConsumeTokenException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws Exception, MerchantDoesNotExistException, NegativeAmountException, BankException, ConsumeTokenException;
}
