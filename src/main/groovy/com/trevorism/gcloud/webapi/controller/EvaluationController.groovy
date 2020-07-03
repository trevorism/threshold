package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.model.MetricRequest
import com.trevorism.gcloud.model.MetricThreshold
import com.trevorism.gcloud.service.DefaultThresholdService
import com.trevorism.gcloud.service.ThresholdService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Api("Evaluation Operations")
@Path("/evaluate")
class EvaluationController {

    private ThresholdService service = new DefaultThresholdService()

    @ApiOperation(value = "Get a list of all Thresholds with the given name")
    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    List<MetricThreshold> findMetrics(@PathParam("name") String name) {
        service.getByName(name)
    }

    @ApiOperation(value = "Evaluate the named threshold against the value")
    @GET
    @Path("{name}/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    List<MetricThreshold> evalutateMetric(@PathParam("name") String name, @PathParam("value") String value) {
        service.evaluateThreshold(name, Double.valueOf(value))
    }

    @ApiOperation(value = "Evaluate the named threshold against the value")
    @POST
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    List<MetricThreshold> evalutateMetric(@PathParam("name") String name, MetricRequest metricRequest) {
        service.evaluateThreshold(name, metricRequest.metricValue)
    }
}
