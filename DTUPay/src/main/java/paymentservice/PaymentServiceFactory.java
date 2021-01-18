package paymentservice;

import accountservice.AccountServiceFactory;
import accountservice.MessageQueueAccountService;
import accountservice.customerservice.ICustomerService;
import accountservice.merchantservice.IMerchantService;
import ports.BankPortFactory;
import reportservice.ReportServiceFactory;
import tokenservice.TokenServiceFactory;

public class PaymentServiceFactory {
    static IPaymentService service;

    public IPaymentService getService() {
        if(service == null) {
            service = new PaymentService(
                    (IMerchantService) new AccountServiceFactory().getService(),
                    (ICustomerService) new AccountServiceFactory().getService(),
                    new TokenServiceFactory().getService(),
                    new BankPortFactory().getPort(),
                    new ReportServiceFactory().getService()
            );
        }
        return service;
    }
}
