package paymentservice;


import Bank.BankException;
import Bank.DTUBankPort;
import Bank.IBank;
import DTO.Merchant;

import java.math.BigDecimal;


public class PaymentService implements IPaymentService {

    private static PaymentService instance;
    public static PaymentService getInstance() {
        if(instance == null) {
            instance = new PaymentService(
                    MessageQueueAccountService.getInstance(),
                    MessageQueueAccountService.getInstance(),
                    new TokenServiceMock(),
                    new DTUBankPort(),
                    new ReportServiceMock()
            );
        }
        return instance;
    }

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
        instance = this; // needed for service tests!
    }

    private boolean isNegative(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0)) < 0;
    }

    @Override
    public void registerPayment(Payment payment) throws TokenDoesNotExistException, MerchantDoesNotExistException, NegativeAmountException, BankException, CustomerDoesNotExistException {
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
