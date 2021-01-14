package paymentservice;
import java.util.List;

import reportservice.IReportService;
import reportservice.ReportService;
import tokenservice.ITokenService;
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
    IReportService reportService = ReportService.instance;

    BankService bankService = new BankServiceService().getBankServicePort();

    public PaymentService(){
    }

    @Override
    public void registerPayment(Payment payment) throws /*MerchantDoesNotExistException, CustomerDoesNotExistException,*/ BankServiceException_Exception {
        //if(!merchantService.merchantExists(merchantAccountId))
        //    throw new MerchantDoesNotExistException(payment.merchantId);
        //if(!customerService.customerExists(customerAccountId))
        //    throw new CustomerDoesNotExistException(payment.customerId);

        String merchantAccountId = merchantService.getMerchantAccountId(payment.merchantId);

        String customerId = tokenService.getCustomerId(payment.customerToken);

        String customerAccountId = customerService.getCustomerAccountId(customerId);

        bankService.transferMoneyFromTo(
                customerAccountId,
                merchantAccountId,
                payment.amount,
                payment.description
        );

        //TODO: This assumes all transfers are successful! Refactor to wrap .transferMoneyFromTo in try-catch and only log successful transfers.
        reportService.registerPayment(payment);
    }

    @Override
    public List<Payment> getPayments() {
        //TODO
        return null;
    }
}
