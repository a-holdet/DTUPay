package accountservice;

import accountservice.customerservice.ICustomerService;
import accountservice.merchantservice.IMerchantService;

public interface IAccountService extends ICustomerService, IMerchantService { }
