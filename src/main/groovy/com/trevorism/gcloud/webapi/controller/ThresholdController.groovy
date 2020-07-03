package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.model.MetricRequest
import com.trevorism.gcloud.model.MetricThreshold
import com.trevorism.gcloud.service.DefaultThresholdService
import com.trevorism.gcloud.service.ThresholdService
import com.trevorism.secure.Secure
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Api("Threshold Operations")
@Path("/threshold")
class ThresholdController {

    private ThresholdService service = new DefaultThresholdService()

    @ApiOperation(value = "Create a new Threshold **Secure")
    @POST
    @Secure
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    MetricThreshold create(MetricThreshold threshold) {
        service.create(threshold)
    }

    @ApiOperation(value = "View a Threshold with the {id}")
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    MetricThreshold getById(@PathParam("id") String id) {
        service.getById(id)
    }

    @ApiOperation(value = "Get a list of all Thresholds")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<MetricThreshold> list() {
        service.list()
    }

    @ApiOperation(value = "Update a Button **Secure")
    @PUT
    @Path("{id}")
    @Secure
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    MetricThreshold update(@PathParam("id") String id, MetricThreshold button) {
        service.update(id, button)
    }

    @ApiOperation(value = "Delete a Button with the {name} **Secure")
    @DELETE
    @Secure
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    MetricThreshold delete(@PathParam("id") String id) {
        service.delete(id)
    }
}
