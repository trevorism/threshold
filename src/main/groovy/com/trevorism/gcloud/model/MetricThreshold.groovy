package com.trevorism.gcloud.model

import groovy.transform.ToString

@ToString
class MetricThreshold {

    String id
    String name
    String description
    String operator
    Double value
}
