package com.trevorism.gcloud.service

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.gcloud.model.MetricThreshold

class DefaultThresholdService implements ThresholdService{

    private Repository<MetricThreshold> repository = new PingingDatastoreRepository<>(MetricThreshold)

    @Override
    MetricThreshold create(MetricThreshold metricThreshold) {
        metricThreshold = ThresholdValidator.validate(metricThreshold)
        repository.create(metricThreshold)
    }

    @Override
    MetricThreshold getById(String id) {
        repository.get(id)
    }

    @Override
    List<MetricThreshold> getByName(String name) {
        repository.list().findAll{
            it.name.toLowerCase() == name.toLowerCase()
        }
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

    @Override
    List<MetricThreshold> evaluateThreshold(String name, Double metricValue) {
        def namedThresholds = getByName(name)
        def result = []
        namedThresholds?.each {
            if(thresholdTriggered(metricValue, it.operator, it.value)){
                result << it
            }
        }
        return result
    }

    boolean thresholdTriggered(double metricValue, String operator, double thresholdValue) {
        if(operator == "=" || operator == "=="){
            return metricValue == thresholdValue
        }

        if(operator == "<="){
            return metricValue <= thresholdValue
        }

        if(operator == "<"){
            return metricValue < thresholdValue
        }

        if(operator == ">="){
            return metricValue >= thresholdValue
        }

        if(operator == ">"){
            return metricValue < thresholdValue
        }

        if(operator == "!=" || operator == "<>"){
            return metricValue != thresholdValue
        }

        false
    }
}
