package DTUPay;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TokenHolder {
    public static TokenHolder instance = new TokenHolder();
    List<UUID> tokens;

    public TokenHolder(){
        reset();
    }

    public void setTokens(List<UUID> tokens){
        this.tokens = tokens;
    }

    public List<UUID> getTokens(){
        return this.tokens;
    }

    public void reset(){
        tokens = new ArrayList<>();
    }
}
