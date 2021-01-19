package paymentservice;

import accountservice.*;
import bank.BankException;
import bank.IBank;
import reportservice.IReportService;
import tokenservice.ITokenService;
import tokenservice.TokenDoesNotExistException;

import java.math.BigDecimal;


public class PaymentService implements IPaymentService {

    private final IMerchantService merchantService;
    private final ICustomerService customerService;
    private final ITokenService tokenService;
    private final IBank bank;
    private final IReportService reportService;

    public PaymentService(IMerchantService merchantService, ICustomerService customerService, ITokenService tokenService, IBank bank, IReportService reportService) {
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
    public void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException, BankException, CustomerDoesNotExistException {
        System.out.println("REGISTER PAYMENT BUSINESS LOGIC!");
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
