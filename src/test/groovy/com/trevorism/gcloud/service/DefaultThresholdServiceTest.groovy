package com.trevorism.gcloud.service

import com.trevorism.gcloud.model.MetricThreshold

import org.junit.Test

class DefaultThresholdServiceTest {

    //@Test
    void testCreate() {
        MetricThreshold metricThreshold = new MetricThreshold(name: "first", description: "bitcoin price", operator: "<=", value: 9000)
        DefaultThresholdService service = new DefaultThresholdService()
        service.create(metricThreshold)
        println service.list()
    }

    //@Test
    void testGetById() {
        DefaultThresholdService service = new DefaultThresholdService()
        assert service.getById("5648831622414336")

    }

    //@Test
    void testList() {
        DefaultThresholdService service = new DefaultThresholdService()
        println service.list()
    }

    //@Test
    void testUpdate() {
        DefaultThresholdService service = new DefaultThresholdService()
        println service.update("5648831622414336", new MetricThreshold(value: 9000))

    }

    //@Test
    void testDelete() {
        DefaultThresholdService service = new DefaultThresholdService()
        service.delete("5686195958841344")

        println service.list()
    }
}
