package com.trevorism.gcloud.model

import groovy.transform.ToString

@ToString
class MetricThreshold {

    String id //must be a long number
    String name
    String description
    String operator
    Double value
}
