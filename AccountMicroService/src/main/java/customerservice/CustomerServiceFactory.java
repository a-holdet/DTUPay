package customerservice;

public class CustomerServiceFactory {
    static ICustomerService service;
    public ICustomerService getService() {
        if(service == null){
            service = new LocalCustomerService(
                    new CustomerInMemoryRepository()
            );
        }
        return service;
    }
}
