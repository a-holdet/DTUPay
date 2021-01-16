import merchantservice.MessageQueueMerchantService;

public class StartUp {

    public static void main(String[] args) throws Exception {
        MessageQueueMerchantService.getInstance();
    }
}