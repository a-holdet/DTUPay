package adapters;

import reportservice.IReportService;
import reportservice.Report;
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
    public Response getAllReports() {
        List<Report> reports = reportService.generateAllReports();
        return Response.ok(reports).build();
    }
}
