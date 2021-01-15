package adapters;

import reportservice.IReportService;
import reportservice.UserReport;
import reportservice.ReportService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchantapi/reports")
public class MerchantReportingResource {
    IReportService reportService = ReportService.instance;
    public MerchantReportingResource() {}

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getReportForMerchant(@QueryParam("id") String merchantId) {
        UserReport report = reportService.generateReportForMerchant(merchantId);
        return Response.ok(report).build();
    }
}

