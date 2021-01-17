package paymentservice;
import java.math.BigDecimal;

import DTO.Payment;
import customerservice.CustomerDoesNotExistException;
import merchantservice.*;
import ports.BankException;
import ports.DTUBankPort;
import ports.IBank;
import reportservice.IReportService;
import reportservice.ReportService;
import customerservice.LocalCustomerService;
import customerservice.ICustomerService;


public class PaymentService implements IPaymentService {
    private final PaymentPortAdapter portAdapter = PaymentPortAdapter.instance;
    private static PaymentService instance;

    public static PaymentService getInstance() {
        if(instance == null) {
            instance = new PaymentService(
                    MessageQueueMerchantService.getInstance(),
                    LocalCustomerService.instance,
                    new DTUBankPort(),
                    ReportService.getInstance()
            );
        }
        return instance;
    }

    private final IMerchantService merchantService;
    private final ICustomerService customerService;
    private final IBank bank;
    private final IReportService reportService;

    public PaymentService(IMerchantService merchantService, ICustomerService customerService, IBank bank, IReportService reportService) {
        this.merchantService = merchantService;
        this.customerService = customerService;
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

        reportService.registerTransaction(payment, customerId);
    }
}
