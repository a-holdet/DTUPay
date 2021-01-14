package paymentservice;

import dtu.ws.fastmoney.BankServiceException_Exception;
import tokenservice.TokenDoesNotExistException;

import java.util.List;

public interface IPaymentService {
    void registerPayment(Payment payment) throws /*MerchantDoesNotExistException, CustomerDoesNotExistException,*/ BankServiceException_Exception, TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException;

    List<Payment> getPayments();
}
