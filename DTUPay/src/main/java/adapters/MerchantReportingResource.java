package adapters;

import accountservice.MerchantDoesNotExistException;
import reportservice.IReportService;
import DTO.UserReport;
import reportservice.ReportServiceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchantapi/reports")
public class MerchantReportingResource {
    IReportService reportService = new ReportServiceFactory().getService();

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getReportForMerchant(@QueryParam("id") String merchantId, @QueryParam("start") String startTime, @QueryParam("end") String endTime) {
        try {
            UserReport report = reportService.generateReportForMerchant(merchantId,startTime,endTime);
            return Response.ok(report).build();
        } catch (MerchantDoesNotExistException e) {
            return Response.status(422).entity(e.getMessage()).build();
        }
    }
}

