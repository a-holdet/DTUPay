package tokenservice;

public class TokenCreationDTO {
    private String userId;
    private int tokenAmount;

    public TokenCreationDTO() {}

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTokenAmount(int tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public String getUserId() {
        return userId;
    }

    public int getTokenAmount() {
        return tokenAmount;
    }
}
