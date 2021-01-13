package paymentservice;

import dtu.ws.fastmoney.BankServiceException_Exception;

import java.util.List;

public interface IPaymentService {
    void registerPayment(Payment payment) throws /*MerchantDoesNotExistException, CustomerDoesNotExistException,*/ BankServiceException_Exception;

    List<Payment> getPayments();
}
