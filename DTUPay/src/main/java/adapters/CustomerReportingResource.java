package adapters;

import reportservice.IReportService;
import reportservice.ReportService;
import reportservice.UserReport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customerapi/reports")
public class CustomerReportingResource {

    IReportService reportService = ReportService.instance;

    public CustomerReportingResource() {}

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getReportForCustomer(@QueryParam("id") String customerId) {
        UserReport report = reportService.generateReportForCustomer(customerId);
        return Response.ok(report).build();
    }
}
