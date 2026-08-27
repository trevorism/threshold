package com.trevorism

import com.google.gson.Gson
import com.trevorism.http.HttpClient
import com.trevorism.http.JsonHttpClient
import com.trevorism.https.AppClientSecureHttpClient
import com.trevorism.https.SecureHttpClient

class ThresholdWorld {

    static final String BASE_URL = System.getenv("ACCEPTANCE_BASE_URL") ?: "https://threshold.action.trevorism.com"
    static final String DESCRIPTION = "acceptance test threshold"

    private final Gson gson = new Gson()
    private final SecureHttpClient authClient = new AppClientSecureHttpClient()
    private final HttpClient anonClient = new JsonHttpClient()

    final List<String> createdIds = []
    final String metricName = "acceptance-${UUID.randomUUID().toString().take(8)}"

    private int expectedNamedCount = 0

    String body
    boolean rejected
    Map lastThreshold
    List<Map> evaluationResult = []
    List<Map> foundThresholds = []

    Map createThreshold(Map threshold) {
        body = authClient.post("${BASE_URL}/threshold/".toString(), gson.toJson(threshold))
        Map created = gson.fromJson(body, Map)
        if (created?.id) {
            createdIds << (created.id as String)
        }
        return created
    }

    Map createNamedThreshold(String operator, String value) {
        lastThreshold = createThreshold([name: metricName, description: DESCRIPTION, operator: operator, value: value as double])
        expectedNamedCount++
        awaitNamedCount(expectedNamedCount)
        return lastThreshold
    }

    void attemptCreateThreshold(Map threshold) {
        try {
            lastThreshold = createThreshold(threshold)
            rejected = false
        }
        catch (Exception ignored) {
            rejected = true
            body = null
        }
    }

    Map fetchById(String id) {
        body = authClient.get("${BASE_URL}/threshold/${id}".toString())
        return gson.fromJson(body, Map)
    }

    boolean retrievable(String id) {
        try {
            return fetchById(id)?.id as boolean
        }
        catch (Exception ignored) {
            return false
        }
    }

    List<Map> listThresholds() {
        body = authClient.get("${BASE_URL}/threshold/".toString())
        return gson.fromJson(body, List) ?: []
    }

    Map updateThreshold(String id, Map threshold) {
        body = authClient.put("${BASE_URL}/threshold/${id}".toString(), gson.toJson(threshold))
        return gson.fromJson(body, Map)
    }

    Map deleteThreshold(String id) {
        body = authClient.delete("${BASE_URL}/threshold/${id}".toString())
        createdIds.remove(id)
        return gson.fromJson(body, Map)
    }

    List<Map> findByName() {
        body = authClient.get("${BASE_URL}/evaluation/${metricName}".toString())
        foundThresholds = gson.fromJson(body, List) ?: []
        return foundThresholds
    }

    List<Map> evaluate(String metricValue) {
        body = authClient.get("${BASE_URL}/evaluation/${metricName}/${metricValue}".toString())
        evaluationResult = gson.fromJson(body, List) ?: []
        return evaluationResult
    }

    List<Map> evaluateByPost(String metricValue) {
        body = authClient.post("${BASE_URL}/evaluation/${metricName}".toString(), gson.toJson([metricValue: metricValue as double]))
        evaluationResult = gson.fromJson(body, List) ?: []
        return evaluationResult
    }

    void anonGet(String path) {
        try {
            body = anonClient.get("${BASE_URL}/${path}".toString())
            rejected = false
        }
        catch (Exception ignored) {
            rejected = true
            body = null
        }
    }

    void anonPost(String path) {
        try {
            body = anonClient.post("${BASE_URL}/${path}".toString(), "{}")
            rejected = false
        }
        catch (Exception ignored) {
            rejected = true
            body = null
        }
    }

    void cleanup() {
        createdIds.each { String id ->
            try {
                authClient.delete("${BASE_URL}/threshold/${id}".toString())
            }
            catch (Exception ignored) {
            }
        }
        createdIds.clear()
    }

    private void awaitNamedCount(int expected) {
        for (int i = 0; i < 15; i++) {
            if (findByName().size() >= expected) {
                return
            }
            Thread.sleep(1000)
        }
        throw new IllegalStateException("Threshold '${metricName}' was not queryable by name after creation")
    }
}
