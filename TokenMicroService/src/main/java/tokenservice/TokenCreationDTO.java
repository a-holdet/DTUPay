package tokenservice;

public class TokenCreationDTO {
    private final String userId;
    private final int tokenAmount;

    public TokenCreationDTO(String userId, int tokenAmount) {
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
