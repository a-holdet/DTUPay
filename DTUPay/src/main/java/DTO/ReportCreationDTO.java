package DTO;

public class ReportCreationDTO {
    private String userId;
    private  String startTime;
    private String endTime;

    public ReportCreationDTO(String userId, String startTime, String endTime) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
