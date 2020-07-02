package com.trevorism.gcloud.service

import com.trevorism.gcloud.model.MetricThreshold

interface ThresholdService {

    MetricThreshold create(MetricThreshold metricThreshold)

    MetricThreshold getById(String id)

    List<MetricThreshold> list()

    MetricThreshold update(String id, MetricThreshold metricThreshold)

    MetricThreshold delete(String id)
}