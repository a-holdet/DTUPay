package paymentservice;

import DTO.Payment;
import customerservice.CustomerDoesNotExistException;
import merchantservice.MerchantDoesNotExistException;
import ports.BankException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, CustomerDoesNotExistException, NegativeAmountException, BankException;
}
