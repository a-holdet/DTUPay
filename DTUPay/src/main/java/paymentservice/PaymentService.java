package paymentservice;
import java.math.BigDecimal;
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

    private boolean isNegative(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0))<0;
    }

    @Override
    public void registerPayment(Payment payment) throws BankServiceException_Exception, TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException {
        String merchantAccountId = merchantService.getMerchantAccountId(payment.merchantId);

        if (merchantAccountId==null)
            throw new MerchantDoesNotExistException("The merchant does not exist in DTUPay");

        if(isNegative(payment.amount))
            throw new NegativeAmountException("Cannot transfer a negative amount");



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
