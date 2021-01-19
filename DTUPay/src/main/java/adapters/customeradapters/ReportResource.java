package adapters.customeradapters;

import reportservice.IReportService;
import DTO.UserReport;
import reportservice.ReportServiceFactory;
import reportservice.UserDoesNotExistsException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customerapi/customers/{id}/reports")
public class ReportResource {
    IReportService reportService = new ReportServiceFactory().getService();

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReportForCustomer(@PathParam("id") String customerId, @QueryParam("start") String startTime, @QueryParam("end") String endTime) {
        try {
            UserReport report = reportService.generateReportForCustomer(customerId, startTime, endTime);
            return Response.ok(report).build();
        } catch (UserDoesNotExistsException e) {
            return Response.status(422).entity(e.getMessage()).build();
        }

    }
}
