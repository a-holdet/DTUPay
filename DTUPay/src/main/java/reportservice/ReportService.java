package reportservice;

import dtu.ws.fastmoney.User;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import paymentservice.Payment;

import java.util.List;

public class ReportService implements IReportService {

    public static ReportService instance = new ReportService();

    private IReportRepository reportRepository = new ReportInMemoryRepository();
    private IMerchantService merchantService = LocalMerchantService.instance;
    private ReportService() {}

    @Override
    public Report generateReportFor(String merchantId) {
        List<Payment> payments = reportRepository.getPaymentsFor(merchantId);
        Merchant merchant = merchantService.getMerchantWith(merchantId);
        System.out.println("MERCHANT:" + merchant);
        User merchantAsUser = new User();
        merchantAsUser.setFirstName(merchant.firstName);
        merchantAsUser.setLastName(merchant.lastName);
        merchantAsUser.setCprNumber(merchant.cprNumber);

        Report report = new Report();
        report.setPayments(payments);
        report.setMerchant(merchantAsUser);

        return report;
    }

    @Override
    public void registerPayment(Payment payment) {
        reportRepository.registerPayment(payment);
    }
}
