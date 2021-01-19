package reportservice;

public class ReportCreationDTO {
    private final String customerId;
    private final String startTime;
    private final String endTime;

    public ReportCreationDTO(String customerId, String startTime, String endTime) {
        this.customerId = customerId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public String getCustomerId() {
        return customerId;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
}
