package paymentservice;

import ports.BankException;

public interface IPaymentService {
    void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException, BankException;
}
