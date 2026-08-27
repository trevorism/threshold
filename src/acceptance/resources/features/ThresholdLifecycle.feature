Feature: Threshold lifecycle
  A KPI threshold can be created, retrieved, updated and deleted over the REST interface

  Background:
    Given the threshold application is alive

  Scenario: A threshold can be created, updated and deleted
    Given a threshold with operator ">" and value 100
    Then the threshold can be retrieved by id
    And the threshold appears in the full list
    When the threshold value is updated to 200
    Then the retrieved threshold has value 200
    When the threshold is deleted
    Then the threshold can no longer be retrieved

  Scenario: A threshold name is stored lowercase and trimmed
    When a threshold is created with the name "  MixedCase-Threshold  "
    Then the stored name is "mixedcase-threshold"
