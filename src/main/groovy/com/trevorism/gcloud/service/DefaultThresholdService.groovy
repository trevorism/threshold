package com.trevorism.gcloud.service

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.gcloud.model.MetricThreshold

class DefaultThresholdService implements ThresholdService{

    private Repository<MetricThreshold> repository = new PingingDatastoreRepository<>(MetricThreshold)

    @Override
    MetricThreshold create(MetricThreshold metricThreshold) {
        repository.create(metricThreshold)
    }

    @Override
    MetricThreshold getById(String id) {
        repository.get(id)
    }

    @Override
    List<MetricThreshold> list() {
        repository.list()
    }

    @Override
    MetricThreshold update(String id, MetricThreshold metricThreshold) {
        repository.update(id, metricThreshold)
    }

    @Override
    MetricThreshold delete(String id) {
        repository.delete(id)
    }
}
