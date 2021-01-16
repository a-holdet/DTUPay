package paymentservice;
import java.math.BigDecimal;

import ports.BankException;
import ports.DTUBankPort;
import ports.IBank;
import reportservice.IReportService;
import reportservice.ReportService;
import customerservice.LocalCustomerService;
import customerservice.ICustomerService;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;


public class PaymentService implements IPaymentService {

    private final PaymentPortAdapter portAdapter = PaymentPortAdapter.instance;
    public static PaymentService instance = new PaymentService();
    IMerchantService merchantService = LocalMerchantService.instance;
    ICustomerService customerService = LocalCustomerService.instance;
//    ITokenService tokenService = TokenService.instance;
    IBank bank = new DTUBankPort();
    IReportService reportService = ReportService.instance;

    //TODO: Make private to ensure singleton-pattern
    public PaymentService(){
    }

    private boolean isNegative(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0))<0;
    }

    @Override
    public void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException, BankException {
        String merchantAccountId = merchantService.getMerchantAccountId(payment.merchantId);
        System.out.println("what");
        if(merchantAccountId==null)
            throw new MerchantDoesNotExistException("The merchant does not exist in DTUPay");

        if(isNegative(payment.amount))
            throw new NegativeAmountException("Cannot transfer a negative amount");


        String customerId = null;
        try {
            System.out.println("portadapter" + portAdapter);
            customerId = portAdapter.consumeToken(payment.customerToken);
        } catch (Exception e) {
            System.out.println("shouldnt happen");
            e.printStackTrace();
        }


        String customerAccountId = customerService.getCustomerAccountId(customerId);

        bank.transferMoneyFromTo(
                customerAccountId,
                merchantAccountId,
                payment.amount,
                payment.description
        );

        //TODO: This assumes all transfers are successful! Refactor to wrap .transferMoneyFromTo in try-catch and only log successful transfers.
        reportService.registerTransaction(payment, customerId);
    }
}
