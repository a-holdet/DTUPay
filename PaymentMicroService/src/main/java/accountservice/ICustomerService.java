package accountservice;

/***
 * @Author Jakob Vestergaard Offersen, s163940
 */
public interface ICustomerService {
    Customer getCustomer(String customerId) throws CustomerDoesNotExistException;
}
