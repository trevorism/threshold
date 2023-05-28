package com.trevorism.gcloud.service

import com.trevorism.data.Repository
import com.trevorism.data.model.filtering.ComplexFilter
import com.trevorism.data.model.paging.PageRequest
import com.trevorism.data.model.sorting.ComplexSort
import com.trevorism.gcloud.model.MetricThreshold
import com.trevorism.https.SecureHttpClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class DefaultThresholdServiceTest {

    private DefaultThresholdService service

    @BeforeEach
    void setup(){
        service = new DefaultThresholdService([get: { s -> return "pong"}] as SecureHttpClient)
        service.repository = createMockRepository()
    }

    @Test
    void testCreate() {
        MetricThreshold metricThreshold = new MetricThreshold(name: "first", description: "bitcoin price", operator: "<=", value: 9000)
        def result = service.create(metricThreshold)
        assert result
        assert result.name == metricThreshold.name
    }

    @Test
    void testGetById() {
        assert service.getById("1")
        assert !service.getById("blah")

    }

    @Test
    void testList() {
        def list = service.list()
        assert list
        assert list.size() == 2
    }

    @Test
    void testUpdate() {
        def updated = service.update("1", new MetricThreshold(name: "first", description: "bitcoin price", operator: "<=", value: 6000))
        assert updated
        assert updated.value == 6000
    }

    @Test
    void testUpdateMissingId() {
        def updated = service.update(null, new MetricThreshold(name: "first", description: "bitcoin price", operator: "<=", value: 6000))
        assert !updated
    }

    @Test
    void testDelete() {
        assert service.delete("1")
        assert !service.delete("blah")
    }

    @Test
    void testGetByName(){
        assert service.getByName("test")
        assert service.getByName("heartbeat")
        assert !service.getByName("blah")
    }

    @Test
    void testEvaluateThreshold(){
        assert service.evaluateThreshold("test", 8692.32)
        assert service.evaluateThreshold("test", 9000)
        assert !service.evaluateThreshold("test", 9692.32)

        assert service.evaluateThreshold("heartbeat", 138)
        assert service.evaluateThreshold("heartbeat", 130)
        assert !service.evaluateThreshold("heartbeat", 122)

    }

    Repository<MetricThreshold> createMockRepository() {
        return new Repository<MetricThreshold>() {

            @Override
            List<MetricThreshold> list() {
                [new MetricThreshold(id: "1", name: "test", operator: "<=", value: 9000),new MetricThreshold(id: "2", name: "heartbeat", operator: ">=", value: 130)]
            }

            @Override
            MetricThreshold get(String s) {
                if(s == "1")
                    return list()[0]
                return null
            }

            @Override
            MetricThreshold create(MetricThreshold metricThreshold) {
                return metricThreshold
            }

            @Override
            MetricThreshold update(String s, MetricThreshold metricThreshold) {
                if(s == "1")
                    return metricThreshold
                return null
            }

            @Override
            MetricThreshold delete(String s) {
                if(s == "1")
                    return list()[0]
                return null
            }

            @Override
            void ping() {

            }

            @Override
            List<MetricThreshold> filter(ComplexFilter complexFilter) {
                return list().findAll{ it.name.toLowerCase() == complexFilter.getSimpleFilters()[0].value}
            }

            @Override
            List<MetricThreshold> page(PageRequest pageRequest) {
                return null
            }

            @Override
            List<MetricThreshold> sort(ComplexSort complexSort) {
                return null
            }
        }
    }
}
