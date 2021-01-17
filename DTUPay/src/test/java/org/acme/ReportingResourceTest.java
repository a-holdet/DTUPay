package org.acme;

import customerservice.Customer;
import customerservice.CustomerDoesNotExistException;
import customerservice.ICustomerService;
import io.quarkus.test.junit.QuarkusTest;
import merchantservice.IMerchantService;
import merchantservice.Merchant;
import merchantservice.MerchantDoesNotExistException;
import org.junit.jupiter.api.Test;
import DTO.Payment;
import reportservice.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class ReportingResourceTest {

    private static class MerchantServiceMock implements IMerchantService {

        private Merchant merchant;
        @Override
        public String registerMerchant(Merchant merchant) throws IllegalArgumentException {
            this.merchant = merchant;
            String id = "Mock Merchant Id";
            merchant.id = id;
            return id;
        }

        @Override
        public Merchant getMerchant(String merchantId) throws MerchantDoesNotExistException {
            return merchant;
        }
    }

    private static class CustomerServiceMock implements ICustomerService {

        private Customer customer;
        @Override
        public String registerCustomer(Customer customer) throws IllegalArgumentException {
            this.customer = customer;
            String id = "Mock Customer id";
            customer.id = id;
            return id;
        }

        @Override
        public boolean customerExists(String customerId) {
            return true;
        }

        @Override
        public Customer getCustomer(String customerId) throws CustomerDoesNotExistException {
            return customer;
        }
    }

    @Test
    public void testMerchantReportWithTimeInterval() {
        LocalDateTime nowMinus4 = LocalDateTime.now().minusDays(4);
        LocalDateTime nowMinus3 = LocalDateTime.now().minusDays(3);
        LocalDateTime now = LocalDateTime.now();

        String customerId = "CUSTOMERID";
        String descriptionT1 = "Old Payment";
        String descriptionT2 = "NEW Payment";


        Merchant merchant = new Merchant("Stein", "Bagger", "121292-0012","ACCOUNTID",null);

        IMerchantService merchantService = new MerchantServiceMock();
        merchant.id = merchantService.registerMerchant(merchant);

        Transaction t1 = new Transaction(UUID.randomUUID(),merchant.id,
                descriptionT1,new BigDecimal(10),
                customerId,LocalDateTime.now().minusDays(4));
        Transaction t2 = new Transaction(UUID.randomUUID(),merchant.id,
                descriptionT2,new BigDecimal(10),
                customerId,LocalDateTime.now().minusDays(2));


        List<Payment> paymentsList = new ArrayList<>();
        paymentsList.add(t1.toPayment());
        paymentsList.add(t2.toPayment());

        ITransactionsRepository transactionsRepository = new TransactionsInMemoryRepository();
        transactionsRepository.registerTransaction(t1);
        transactionsRepository.registerTransaction(t2);

        new ReportService(transactionsRepository, merchantService, new CustomerServiceMock());

        /* TODO: fix for windows pcs?
        given().pathParam("id",merchant.id).pathParam("start",nowMinus4.toString()).pathParam("end",nowMinus3.toString())
               .when().get("/merchantapi/reports?id={id}&start={start}&end={end}")
               .then()
               .statusCode(200)
               .body(not(containsString(descriptionT2))).body(containsString(descriptionT1));
        */
        given().pathParam("id",merchant.id).pathParam("start",nowMinus3.toString()).pathParam("end",now.toString())
                .when().get("/merchantapi/reports?id={id}&start={start}&end={end}")
                .then()
                .statusCode(200)
                .body(not(containsString(descriptionT1)))
                .body(containsString(descriptionT2));

        given().pathParam("id",merchant.id).pathParam("end",now.toString())
                .when().get("/merchantapi/reports?id={id}&end={end}")
                .then()
                .statusCode(200)
                .body(containsString(descriptionT1))
                .body(containsString(descriptionT2));

        given().pathParam("id",merchant.id).pathParam("start",nowMinus3.toString())
                .when().get("/merchantapi/reports?id={id}&start={start}")
                .then()
                .statusCode(200)
                .body(not(containsString(descriptionT1)))
                .body(containsString(descriptionT2));
    }

    @Test
    public void testCustomerReportWithTimeInterval() {
        LocalDateTime nowMinus4 = LocalDateTime.now().minusDays(4);
        LocalDateTime nowMinus3 = LocalDateTime.now().minusDays(3);
        LocalDateTime now = LocalDateTime.now();

        String merchantId = "merchantId";
        String descriptionT1 = "Old Payment";
        String descriptionT2 = "NEW Payment";

        Customer customer = new Customer();
        customer.accountId = "CustomerAccountID";
        customer.firstName = "Joe";
        customer.lastName = "Exotic";
        customer.cprNumber = "121294-0014";

        ICustomerService customerService = new CustomerServiceMock();
        customer.id = customerService.registerCustomer(customer);


        Transaction t1 = new Transaction(UUID.randomUUID(),merchantId,
                descriptionT1,new BigDecimal(10),
                customer.id,LocalDateTime.now().minusDays(4));
        Transaction t2 = new Transaction(UUID.randomUUID(),merchantId,
                descriptionT2,new BigDecimal(10),
                customer.id,LocalDateTime.now().minusDays(2));

        ITransactionsRepository transactionsRepository = new TransactionsInMemoryRepository();
        transactionsRepository.registerTransaction(t1);
        transactionsRepository.registerTransaction(t2);

        new ReportService(transactionsRepository, new MerchantServiceMock(), customerService);

        /* TODO: fix for windows pcs?
        given().pathParam("id",customer.id).pathParam("start",nowMinus4.toString()).pathParam("end",nowMinus3.toString())
                .when().get("/customerapi/reports?id={id}&start={start}&end={end}")
                .then()
                .statusCode(200)
                .body(not(containsString(descriptionT2))).body(containsString(descriptionT1));
        */
        given().pathParam("id",customer.id).pathParam("start",nowMinus3.toString()).pathParam("end",now.toString())
                .when().get("/customerapi/reports?id={id}&start={start}&end={end}")
                .then()
                .statusCode(200)
                .body(not(containsString(descriptionT1)))
                .body(containsString(descriptionT2));

        given().pathParam("id",customer.id).pathParam("end",now.toString())
                .when().get("/customerapi/reports?id={id}&end={end}")
                .then()
                .statusCode(200)
                .body(containsString(descriptionT1))
                .body(containsString(descriptionT2));

        given().pathParam("id",customer.id).pathParam("start",nowMinus3.toString())
                .when().get("/customerapi/reports?id={id}&start={start}")
                .then()
                .statusCode(200)
                .body(not(containsString(descriptionT1)))
                .body(containsString(descriptionT2));
    }
}