package DTUPay.Holders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TokenHolder {
    private List<UUID> tokens;

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
        setTokens(new ArrayList<>());
    }
}
