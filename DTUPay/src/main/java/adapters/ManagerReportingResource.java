package adapters;

import reportservice.IReportService;
import reportservice.Transaction;
import reportservice.UserReport;
import reportservice.ReportService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/managerapi/reports")
public class ManagerReportingResource {
    IReportService reportService = ReportService.instance;

    public ManagerReportingResource() {}

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateManagerOverview() {
        List<Transaction> transactions = reportService.generateManagerOverview();
        return Response.ok(transactions).build();
    }
}
