package paymentservice;
import java.math.BigDecimal;
import java.util.List;

import customerservice.CustomerService;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import merchantservice.MerchantService;


public class PaymentService {
    public static PaymentService instance = new PaymentService();
    MerchantService merchantService = MerchantService.instance;
    CustomerService customerService = CustomerService.instance;

    BankService bankService = new BankServiceService().getBankServicePort();

    public PaymentService(){
    }

<<<<<<< HEAD
    public void registerPayment(Payment payment) throws MerchantDoesNotExistException, CustomerDoesNotExistException {
        String customerAccountId = payment.customerId;
        String merchantAccountId = payment.merchantId;
        String description = payment.description;
        int amount = payment.amount;

        if(!merchantService.merchantExists(merchantAccountId))
            throw new MerchantDoesNotExistException(payment.merchantId);
        if(!customerService.customerExists(customerAccountId))
            throw new CustomerDoesNotExistException(payment.customerId);

        try {
            bankService.transferMoneyFromTo(customerAccountId,merchantAccountId,new BigDecimal(amount),description);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
=======
    public void registerPayment(Payment payment) throws /*MerchantDoesNotExistException, CustomerDoesNotExistException,*/ BankServiceException_Exception {
        //if(!merchantService.merchantExists(merchantAccountId))
        //    throw new MerchantDoesNotExistException(payment.merchantId);
        //if(!customerService.customerExists(customerAccountId))
        //    throw new CustomerDoesNotExistException(payment.customerId);

        bankService.transferMoneyFromTo(
                payment.customerAccountId,
                payment.merchantAccountId,
                payment.amount,
                payment.description
        );
>>>>>>> e0ff9421f9507f13e7e81007ae76973b62b24156
    }

    public List<Payment> getPayments() {
        //TODO
        return null;
    }
}
