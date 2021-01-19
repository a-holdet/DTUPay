package adapters.merchantadapters;

import accountservice.MerchantDoesNotExistException;
import reportservice.IReportService;
import DTO.UserReport;
import reportservice.ReportServiceFactory;
import reportservice.UserDoesNotExistsException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("/merchantapi/merchants/{id}/reports")
public class ReportingResource {
    IReportService reportService = new ReportServiceFactory().getService();

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getReportForMerchant(@PathParam("id") String merchantId, @QueryParam("start") String startTime, @QueryParam("end") String endTime) {
        try {
            UserReport report = reportService.generateReportForMerchant(merchantId,startTime,endTime);
            return Response.ok(report).build();
        } catch (UserDoesNotExistsException e) {
            return Response.status(422).entity(e.getMessage()).build();
        }
    }
}

