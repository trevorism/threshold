package com.trevorism.gcloud

import com.trevorism.ThresholdWorld

this.metaClass.mixin(io.cucumber.groovy.Hooks)
this.metaClass.mixin(io.cucumber.groovy.EN)

World {
    new ThresholdWorld()
}

After { ->
    cleanup()
}

Given(~/^a threshold with operator "(.*)" and value (-?\d+(?:\.\d+)?)$/) { String operator, String value ->
    createNamedThreshold(operator, value)
}

Given(~/^another threshold with operator "(.*)" and value (-?\d+(?:\.\d+)?)$/) { String operator, String value ->
    createNamedThreshold(operator, value)
}

When(~/^the threshold is evaluated with the value (-?\d+(?:\.\d+)?)$/) { String value ->
    evaluate(value)
}

When(~/^the threshold is evaluated by posting the value (-?\d+(?:\.\d+)?)$/) { String value ->
    evaluateByPost(value)
}

When(~/^the thresholds are looked up by name$/) { ->
    findByName()
}

When(~/^the threshold value is updated to (-?\d+(?:\.\d+)?)$/) { String value ->
    Map current = fetchById(lastThreshold.id as String)
    current.value = value as double
    updateThreshold(lastThreshold.id as String, current)
}

When(~/^the threshold is deleted$/) { ->
    deleteThreshold(lastThreshold.id as String)
}

When(~/^a threshold is created with the name "(.*)"$/) { String name ->
    attemptCreateThreshold([name: name, description: ThresholdWorld.DESCRIPTION, operator: ">", value: 1d])
}

When(~/^a threshold is created without a name$/) { ->
    attemptCreateThreshold([description: ThresholdWorld.DESCRIPTION, operator: ">", value: 1d])
}

When(~/^a threshold is created without a value$/) { ->
    attemptCreateThreshold([name: metricName, description: ThresholdWorld.DESCRIPTION, operator: ">"])
}

When(~/^a threshold is created with the operator "(.*)"$/) { String operator ->
    attemptCreateThreshold([name: metricName, description: ThresholdWorld.DESCRIPTION, operator: operator, value: 1d])
}

When(~/^a threshold is created without an operator$/) { ->
    attemptCreateThreshold([name: metricName, description: ThresholdWorld.DESCRIPTION, value: 1d])
}

When(~/^I GET "(.*)" anonymously$/) { String path ->
    anonGet(path)
}

When(~/^I POST "(.*)" anonymously$/) { String path ->
    anonPost(path)
}

Then(~/^(\d+) thresholds? (?:is|are) triggered$/) { Integer count ->
    assert evaluationResult.size() == count
}

Then(~/^(\d+) thresholds? (?:is|are) found$/) { Integer count ->
    assert foundThresholds.size() == count
}

Then(~/^the triggered threshold has value (-?\d+(?:\.\d+)?)$/) { String value ->
    assert evaluationResult.size() == 1
    assert evaluationResult[0].value == (value as double)
}

Then(~/^the threshold can be retrieved by id$/) { ->
    Map found = fetchById(lastThreshold.id as String)
    assert found.id == lastThreshold.id
    assert found.name == metricName
}

Then(~/^the threshold appears in the full list$/) { ->
    assert listThresholds().any { it.id == lastThreshold.id }
}

Then(~/^the retrieved threshold has value (-?\d+(?:\.\d+)?)$/) { String value ->
    assert fetchById(lastThreshold.id as String).value == (value as double)
}

Then(~/^the threshold can no longer be retrieved$/) { ->
    assert !retrievable(lastThreshold.id as String)
}

Then(~/^the stored name is "(.*)"$/) { String expected ->
    assert !rejected
    assert lastThreshold.name == expected
}

Then(~/^the threshold is created successfully$/) { ->
    assert !rejected
    assert lastThreshold.id
}

Then(~/^the request is rejected$/) { ->
    assert rejected
}

Then(~/^the response body is "(.*)"$/) { String expected ->
    assert body?.trim() == expected
}
