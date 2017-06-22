<#if jerseyStyle>
package ${basePackageName}.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
<#else>
package ${basePackageName}.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public final class SampleController {
    private static final Logger log = LoggerFactory.getLogger(SampleController.class);

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity home(@RequestParam("id") Long id) {
        log.info("Hello world!");
        return ResponseEntity.ok().build();
    }
}
</#if>