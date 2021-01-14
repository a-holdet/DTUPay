package reportservice;

public class ReportService implements IReportService {

    public static ReportService instance = new ReportService();

    private ReportService() {}

    @Override
    public Report generateReportFor(String merchantId) {
        return null;
    }
}
