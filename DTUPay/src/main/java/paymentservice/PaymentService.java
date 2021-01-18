package paymentservice;
import java.math.BigDecimal;

import DTO.Payment;
import accountservice.merchantservice.IMerchantService;
import accountservice.merchantservice.Merchant;
import accountservice.merchantservice.MerchantDoesNotExistException;
import ports.BankPortException;
import ports.IBankPort;
import reportservice.IReportService;
import tokenservice.ConsumeTokenException;
import tokenservice.ITokenService;
import accountservice.customerservice.ICustomerService;


public class PaymentService implements IPaymentService {

    private final IMerchantService merchantService;
    private final ICustomerService customerService;
    private final ITokenService tokenService;
    private final IBankPort bank;
    private final IReportService reportService;

    public PaymentService(IMerchantService merchantService, ICustomerService customerService, ITokenService tokenService, IBankPort bank, IReportService reportService) {
        this.merchantService = merchantService;
        this.customerService = customerService;
        this.tokenService = tokenService;
        this.bank = bank;
        this.reportService = reportService;
    }

    private boolean isNegative(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0)) < 0;
    }

    @Override
    public void registerPayment(Payment payment) throws Exception, MerchantDoesNotExistException, NegativeAmountException, BankPortException, ConsumeTokenException {
        System.out.println("-");
        if (isNegative(payment.amount)) throw new NegativeAmountException("Cannot transfer a negative amount");

        Merchant merchant = merchantService.getMerchant(payment.merchantId);
        String merchantAccountId = merchant.accountId;


        String customerId = tokenService.consumeToken(payment.customerToken);

        String customerAccountId = customerService.getCustomer(customerId).accountId;

        bank.transferMoneyFromTo(
                customerAccountId,
                merchantAccountId,
                payment.amount,
                payment.description
        );

        reportService.registerTransaction(payment, customerId);
    }
}
