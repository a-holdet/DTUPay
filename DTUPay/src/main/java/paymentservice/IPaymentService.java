package paymentservice;

import DTO.Payment;
import accountservice.merchantservice.MerchantDoesNotExistException;
import ports.BankPortException;
import tokenservice.ConsumeTokenException;

public interface IPaymentService {
    void registerPayment(Payment payment)
            throws Exception,
            MerchantDoesNotExistException,
            NegativeAmountException,
            ConsumeTokenException,
            BankPortException;
}
