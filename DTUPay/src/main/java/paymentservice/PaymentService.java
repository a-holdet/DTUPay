package paymentservice;
import java.math.BigDecimal;

import DTO.Payment;
import customerservice.CustomerDoesNotExistException;
import merchantservice.Merchant;
import merchantservice.MerchantDoesNotExistException;
import ports.BankException;
import ports.DTUBankPort;
import ports.IBank;
import reportservice.IReportService;
import reportservice.ReportService;
import tokenservice.ITokenService;
import tokenservice.TokenDoesNotExistException;
import tokenservice.TokenService;
import customerservice.LocalCustomerService;
import customerservice.ICustomerService;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;


public class PaymentService implements IPaymentService {
    public static PaymentService instance = new PaymentService();

    private final IMerchantService merchantService;
    private final ICustomerService customerService;
    private final ITokenService tokenService;
    private final IBank bank;
    private final IReportService reportService;

    private PaymentService(IMerchantService merchantService, ICustomerService customerService, ITokenService tokenService, IBank bank, IReportService reportService) {
        this.merchantService = merchantService;
        this.customerService = customerService;
        this.tokenService = tokenService;
        this.bank = bank;
        this.reportService = reportService;
    }

    private PaymentService() {
        this(LocalMerchantService.instance, LocalCustomerService.instance, TokenService.instance, new DTUBankPort(), ReportService.instance);
    }

    private boolean isNegative(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0)) < 0;
    }

    @Override
    public void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException, BankException, CustomerDoesNotExistException {
        if (isNegative(payment.amount)) throw new NegativeAmountException("Cannot transfer a negative amount");

        Merchant merchant = merchantService.getMerchant(payment.merchantId);

        String merchantAccountId = merchant.accountId;
        String customerId = tokenService.consumeToken(payment.customerToken);
        String customerAccountId = customerService.getCustomerAccountId(customerId);

        bank.transferMoneyFromTo(
                customerAccountId,
                merchantAccountId,
                payment.amount,
                payment.description
        );

        reportService.registerTransaction(payment, customerId);
    }
}
