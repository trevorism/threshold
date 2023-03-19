package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.model.MetricRequest
import com.trevorism.gcloud.model.MetricThreshold
import com.trevorism.gcloud.service.DefaultThresholdService
import com.trevorism.gcloud.service.ThresholdService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/evaluation")
class EvaluationController {

    private ThresholdService service = new DefaultThresholdService()

    @Tag(name = "Evaluation Operations")
    @Operation(summary = "Get a list of all Thresholds with the given name")
    @Get(value = "{name}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    List<MetricThreshold> findMetrics(String name) {
        service.getByName(name)
    }

    @Tag(name = "Evaluation Operations")
    @Operation(summary = "Evaluate the named threshold against the value")
    @Get(value = "{name}/{value}", produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    List<MetricThreshold> evalutateMetric(String name, String value) {
        service.evaluateThreshold(name, Double.valueOf(value))
    }

    @Tag(name = "Evaluation Operations")
    @Operation(summary = "Evaluate the named threshold against the value")
    @Post(value = "{name}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @Secure(value = Roles.USER, allowInternal = true)
    List<MetricThreshold> evalutateMetric(String name, @Body MetricRequest metricRequest) {
        service.evaluateThreshold(name, metricRequest.metricValue)
    }
}
