package com.trevorism.gcloud.service

import com.trevorism.gcloud.error.ThresholdCreationException
import com.trevorism.gcloud.model.MetricThreshold

class ThresholdValidator {

    private static List<String> validOperators = ["=", "==", "!=", "<>", "<=", ">=", "<", ">"]

    static MetricThreshold validate(MetricThreshold metricThreshold){
        if(!metricThreshold.name){
            throw new ThresholdCreationException("name must exist")
        }
        metricThreshold.name = metricThreshold.name.toLowerCase().trim()

        if(!validOperators.contains(metricThreshold.operator)){
            throw new ThresholdCreationException("operator must be one of these operators: ${validOperators}")
        }

        if(metricThreshold.value == null)
            throw new ThresholdCreationException("value must be a number")

        return metricThreshold
    }
}
