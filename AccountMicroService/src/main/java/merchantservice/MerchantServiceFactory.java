package merchantservice;

public class MerchantServiceFactory {
    static IMerchantService service;
    public IMerchantService getService() {
        if(service == null){
            service = new LocalMerchantService(
                    new MerchantInMemoryRepository()
            );
        }
        return service;
    }
}
