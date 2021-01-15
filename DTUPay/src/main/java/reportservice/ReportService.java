package reportservice;

import DTO.DTUPayUser;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import paymentservice.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReportService implements IReportService {

    public static ReportService instance = new ReportService();

    private IPaymentsRepository paymentsRepository = new PaymentsInMemoryRepository();
    private IMerchantService merchantService = LocalMerchantService.instance;
    private ReportService() {}

    @Override
    public Report generateReportFor(String merchantId) {
        List<Payment> payments = paymentsRepository.getPaymentsFor(merchantId);
        Merchant merchant = merchantService.getMerchantWith(merchantId);
        DTUPayUser merchantAsUser = new DTUPayUser(merchant.firstName, merchant.lastName, merchant.cprNumber, merchant.accountId);
        Report report = new Report();
        report.setPayments(payments);
        report.setUser(merchantAsUser);

        return report;
    }

    @Override
    public void registerPayment(Payment payment) {
        paymentsRepository.registerPayment(payment);
    }

    @Override
    public List<Report> generateAllReports() {
        List<Payment> allPayments = paymentsRepository.getAllPayments();
        List<Merchant> allMerchants = merchantService.getAllMerchants();

        System.out.println("AAAA");
        System.out.println(allPayments.get(0));
        System.out.println(allPayments.get(1));
        System.out.println(allPayments.get(2));
        System.out.println(allPayments.get(3));
        System.out.println(allPayments.size());

        Set<String> uniqueMerchantIDs = allPayments.stream().map(payment -> payment.merchantId).collect(Collectors.toSet());
        Set<Merchant> uniqueMerchants = allMerchants.stream().filter(merchant -> uniqueMerchantIDs.contains(merchant.id)).collect(Collectors.toSet());

        List<Report> reports = new ArrayList<>();

        uniqueMerchants.forEach(merchant -> {
            List<Payment> paymentsForMerchant = allPayments.stream().filter(p -> p.merchantId.equals(merchant.id)).collect(Collectors.toList());
            System.out.println("----");
            System.out.println(paymentsForMerchant.get(0).customerToken);

            Report report = new Report();
            DTUPayUser merchantAsUser = new DTUPayUser(merchant.firstName, merchant.lastName, merchant.cprNumber, merchant.accountId);
            report.setUser(merchantAsUser);
            report.setPayments(paymentsForMerchant);
            reports.add(report);
        });

        return reports;
    }
}
