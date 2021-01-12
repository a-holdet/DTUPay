package BusinessLogic;

import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    public static PaymentService instance = new PaymentService();
    MerchantService merchantService = MerchantService.instance;
    CustomerService customerService = CustomerService.instance;

    List<Payment> payments = new ArrayList<>();


    public PaymentService(){
        System.out.println("def");
    }

    public void registerPayment(Payment payment) throws MerchantDoesNotExistException, CustomerDoesNotExistException {
        if(!merchantService.merchantExists(payment.merchantId))
            throw new MerchantDoesNotExistException(payment.merchantId);
        if(!customerService.customerExists(payment.customerId))
            throw new CustomerDoesNotExistException(payment.customerId);

        payments.add(payment);
    }

    public List<Payment> getPayments() {
        return payments;
    }
}
