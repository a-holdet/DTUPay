package paymentservice;
import java.util.List;

import tokenservice.ITokenService;
import tokenservice.TokenDoesNotExistException;
import tokenservice.TokenService;
import customerservice.LocalCustomerService;
import customerservice.ICustomerService;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;


public class PaymentService implements IPaymentService {
    public static PaymentService instance = new PaymentService();
    IMerchantService merchantService = LocalMerchantService.instance;
    ICustomerService customerService = LocalCustomerService.instance;
    ITokenService tokenService = TokenService.instance;

    BankService bankService = new BankServiceService().getBankServicePort();

    public PaymentService(){
    }

    @Override
    public void registerPayment(Payment payment) throws BankServiceException_Exception, TokenDoesNotExistException {
        String merchantAccountId = merchantService.getMerchantAccountId(payment.merchantId);

        //null check on merchantAccountId (if null, then we throw illegal argument exception or something)
            //throw new MerchantDoesNotExistException(payment.merchantId);

        String customerId = tokenService.consumeToken(payment.customerToken);

        String customerAccountId = customerService.getCustomerAccountId(customerId);

        bankService.transferMoneyFromTo(
                customerAccountId,
                merchantAccountId,
                payment.amount,
                payment.description
        );
    }

    @Override
    public List<Payment> getPayments() {
        //TODO
        return null;
    }
}
