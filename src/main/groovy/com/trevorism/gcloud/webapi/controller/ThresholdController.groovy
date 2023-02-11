package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.model.MetricThreshold
import com.trevorism.gcloud.service.DefaultThresholdService
import com.trevorism.gcloud.service.ThresholdService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/threshold")
class ThresholdController {

    private ThresholdService service = new DefaultThresholdService()

    @Tag(name = "Threshold Operations")
    @Operation(summary = "Create a new Threshold **Secure")
    @Secure(Roles.SYSTEM)
    @Post(value = "/", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    MetricThreshold create(@Body MetricThreshold threshold) {
        service.create(threshold)
    }

    @Tag(name = "Threshold Operations")
    @Operation(summary = "View a Threshold with the {id} **Secure")
    @Get(value = "{id}", produces = MediaType.APPLICATION_JSON)
    MetricThreshold getById(String id) {
        service.getById(id)
    }

    @Tag(name = "Threshold Operations")
    @Operation(summary = "Get a list of all Thresholds **Secure")
    @Get(value = "/", produces = MediaType.APPLICATION_JSON)
    List<MetricThreshold> list() {
        service.list()
    }

    @Tag(name = "Threshold Operations")
    @Operation(summary = "Update a Threshold **Secure")
    @Secure(Roles.SYSTEM)
    @Put(value = "{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    MetricThreshold update(String id, @Body MetricThreshold button) {
        service.update(id, button)
    }

    @Tag(name = "Threshold Operations")
    @Operation(summary = "Delete a Threshold with the {id} **Secure")
    @Secure(Roles.SYSTEM)
    @Delete(value = "{id}", produces = MediaType.APPLICATION_JSON)
    MetricThreshold delete(String id) {
        service.delete(id)
    }
}
