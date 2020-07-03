package com.trevorism.gcloud.service

import com.trevorism.gcloud.model.MetricThreshold

interface ThresholdService {

    MetricThreshold create(MetricThreshold metricThreshold)

    MetricThreshold getById(String id)

    List<MetricThreshold> getByName(String name)

    List<MetricThreshold> list()

    MetricThreshold update(String id, MetricThreshold metricThreshold)

    MetricThreshold delete(String id)

    List<MetricThreshold> evaluateThreshold(String name, Double metricValue)
}