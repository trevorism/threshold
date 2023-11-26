package com.trevorism.gcloud.service

import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.data.model.filtering.FilterBuilder
import com.trevorism.data.model.filtering.FilterConstants
import com.trevorism.data.model.filtering.SimpleFilter
import com.trevorism.gcloud.model.MetricThreshold
import com.trevorism.https.SecureHttpClient

@jakarta.inject.Singleton
class DefaultThresholdService implements ThresholdService{

    private Repository<MetricThreshold> repository

    DefaultThresholdService(SecureHttpClient secureHttpClient){
        repository = new PingingDatastoreRepository<>(MetricThreshold, secureHttpClient)
    }

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
        repository.filter(new FilterBuilder().addFilter(new SimpleFilter("name", FilterConstants.OPERATOR_EQUAL, name)).build())
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

    private static boolean thresholdTriggered(double metricValue, String operator, double thresholdValue) {
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
            return metricValue > thresholdValue
        }

        if(operator == "!=" || operator == "<>"){
            return metricValue != thresholdValue
        }

        false
    }
}
