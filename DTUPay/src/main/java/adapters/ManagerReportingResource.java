package adapters;

import reportservice.IReportService;
import reportservice.MessageQueueReportService;
import DTO.Transaction;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
@Path("/managerapi/reports")
public class ManagerReportingResource {
    IReportService reportService = MessageQueueReportService.getInstance();

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateManagerOverview() {
        List<Transaction> transactions = reportService.generateManagerOverview();
        return Response.ok(transactions).build();
    }
}
