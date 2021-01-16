package org.acme;

import DTO.DTUPayUser;
import com.google.gson.Gson;
import customerservice.LocalCustomerService;
import io.cucumber.messages.IdGenerator;
import io.quarkus.test.junit.QuarkusTest;
import merchantservice.IMerchantService;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import org.junit.jupiter.api.Test;
import paymentservice.Payment;
import reportservice.*;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.eclipse.persistence.config.QueryType.Report;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class ReportingResourceTest {

    @Test
    public void testMerchantReportWithTimeInterval() {
        LocalDateTime nowMinus4 = LocalDateTime.now().minusDays(4);
        LocalDateTime nowMinus3 = LocalDateTime.now().minusDays(3);
        LocalDateTime now = LocalDateTime.now();

        String customerId = "CUSTOMERID";
        String descriptionT1 = "Old Payment";
        String descriptionT2 = "NEW Payment";


        Merchant merchant = new Merchant("Stein", "Bagger", "121292-0012","ACCOUNTID",null);
        DTUPayUser merchantDTU = new DTUPayUser("Stein", "Bagger", "121292-0012","ACCOUNTID");

        IMerchantService merchantService = new LocalMerchantService();
        merchant.id = merchantService.registerMerchant(merchant);

        Transaction t1 = new Transaction(UUID.randomUUID(),merchant.id,
                descriptionT1,new BigDecimal(10),
                customerId,LocalDateTime.now().minusDays(4));
        Transaction t2 = new Transaction(UUID.randomUUID(),merchant.id,
                descriptionT2,new BigDecimal(10),
                customerId,LocalDateTime.now().minusDays(2));

        UserReport merchantReport = new UserReport();

        merchantReport.setUser(merchantDTU);
        List<Payment> paymentsList = new ArrayList<>();
        paymentsList.add(t1.toPayment());
        paymentsList.add(t2.toPayment());
        merchantReport.setPayments(paymentsList);

        ITransactionsRepository transactionsRepository = new TransactionsInMemoryRepository();
        transactionsRepository.registerTransaction(t1);
        transactionsRepository.registerTransaction(t2);

        ReportService.instance = new ReportService(transactionsRepository,merchantService, LocalCustomerService.instance);

        given().pathParam("id",merchant.id).pathParam("start",nowMinus4.toString()).pathParam("end",nowMinus3.toString())
               .when().get("/merchantapi/reports?id={id}&start={start}&end={end}")
               .then()
               .statusCode(200)
               .body(not(containsString(descriptionT2))).body(containsString(descriptionT1));

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
}
