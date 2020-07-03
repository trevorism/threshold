package com.trevorism.gcloud.service

import com.trevorism.gcloud.error.ThresholdCreationException
import com.trevorism.gcloud.model.MetricThreshold
import org.junit.Test

class ThresholdValidatorTest {

    @Test
    void testValidate() {
        assert ThresholdValidator.validate(new MetricThreshold(name: "test1", value: 84, operator: "="))
        assert ThresholdValidator.validate(new MetricThreshold(name: "test2", value: 0, operator: "!="))
        assert ThresholdValidator.validate(new MetricThreshold(name: "test3", value: -30, operator: "<="))
        assert ThresholdValidator.validate(new MetricThreshold(name: "test4", value: 4.4, operator: ">"))
    }

    @Test(expected = ThresholdCreationException)
    void testInvalidName() {
        assert ThresholdValidator.validate(new MetricThreshold(value: 84, operator: "="))
    }

    @Test(expected = ThresholdCreationException)
    void testInvalidOperator() {
        assert ThresholdValidator.validate(new MetricThreshold(name: "test", value: 0, operator: "!=="))
    }

    @Test(expected = ThresholdCreationException)
    void testInvalidValue() {
        assert ThresholdValidator.validate(new MetricThreshold(name: "test", value: null, operator: "!="))
    }
}
