package DTO;

public class TokenCreation {
    private String userId;
    private int tokenAmount;

    public TokenCreation() {}

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
