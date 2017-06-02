package ${basePackageName}.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public final class SampleController {
    private static final Logger log = LoggerFactory.getLogger(SampleController.class);

    @GET
    public Response home() {
        log.info("Hello world!");
        return Response.ok().build();
    }
}