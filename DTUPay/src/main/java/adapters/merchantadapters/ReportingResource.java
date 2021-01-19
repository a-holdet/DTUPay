package adapters.merchantadapters;

import services.reportservice.IReportService;
import DTO.UserReport;
import services.reportservice.ReportServiceFactory;
import services.reportservice.UserDoesNotExistsException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/***
 * @Author Christian Dan Hjelmslund, s164412
 */

@Path("/merchantapi/reports")
public class ReportingResource {
    IReportService reportService = new ReportServiceFactory().getService();

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getReportForMerchant(@QueryParam("id") String merchantId, @QueryParam("start") String startTime, @QueryParam("end") String endTime) {
        try {
            UserReport report = reportService.generateReportForMerchant(merchantId,startTime,endTime);
            return Response.ok(report).build();
        } catch (UserDoesNotExistsException e) {
            return Response.status(422).entity(e.getMessage()).build();
        }
    }
}

