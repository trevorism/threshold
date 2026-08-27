Feature: Security
  The threshold and evaluation endpoints must reject unauthenticated callers

  Background:
    Given the threshold application is alive

  Scenario: Ping is publicly available
    When I GET "ping" anonymously
    Then the response body is "pong"

  Scenario: Listing thresholds requires authentication
    When I GET "threshold/" anonymously
    Then the request is rejected

  Scenario: Creating a threshold requires authentication
    When I POST "threshold/" anonymously
    Then the request is rejected

  Scenario: Viewing a threshold by id requires authentication
    When I GET "threshold/anything" anonymously
    Then the request is rejected

  Scenario: Looking up thresholds by name requires authentication
    When I GET "evaluation/anything" anonymously
    Then the request is rejected

  Scenario: Evaluating a metric value requires authentication
    When I GET "evaluation/anything/1" anonymously
    Then the request is rejected

  Scenario: Posting a metric value requires authentication
    When I POST "evaluation/anything" anonymously
    Then the request is rejected
