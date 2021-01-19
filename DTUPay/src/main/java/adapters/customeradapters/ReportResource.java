package adapters.customeradapters;

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
 * @Author Michael Davidsen Kirkegaard, s153587
 */


@Path("/customerapi/reports")
public class ReportResource {
    IReportService reportService = new ReportServiceFactory().getService();

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getReportForCustomer(@QueryParam("id") String customerId, @QueryParam("start") String startTime, @QueryParam("end") String endTime) {
        try {
            UserReport report = reportService.generateReportForCustomer(customerId, startTime, endTime);
            return Response.ok(report).build();
        } catch (UserDoesNotExistsException e) {
            return Response.status(422).entity(e.getMessage()).build();
        }

    }
}
