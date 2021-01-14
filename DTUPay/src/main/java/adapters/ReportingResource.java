package adapters;

import reportservice.IReportService;
import reportservice.Report;
import reportservice.ReportService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchantapi/reports")
public class ReportingResource {
    IReportService reportService = ReportService.instance;
    public ReportingResource() {}

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getReportForMerchant(@QueryParam("id") String merchantId) {
        Report report = reportService.generateReportFor(merchantId);
        return Response.ok(report).build();
    }
}

