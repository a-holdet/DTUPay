package paymentservice;

import Bank.BankException;
import DTO.Payment;
import merchantservice.MerchantDoesNotExistException;
import tokenservice.ConsumeTokenException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws Exception, MerchantDoesNotExistException, NegativeAmountException, BankException, ConsumeTokenException;
}
