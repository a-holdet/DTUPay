package tokenservice.tokenservice;

public class TokenCreation {
    private final String userId;
    private final int tokenAmount;

    public TokenCreation(String userId, int tokenAmount) {
        this.userId = userId;
        this.tokenAmount = tokenAmount;
    }

    public String getUserId() {
        return userId;
    }

    public int getTokenAmount() {
        return tokenAmount;
    }
}
