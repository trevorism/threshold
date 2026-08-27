Feature: Evaluation of a metric against its thresholds
  Clients check current state by evaluating a metric value against the thresholds registered under a name

  Background:
    Given the threshold application is alive

  Scenario Outline: A threshold of 100 with the operator <operator> evaluated against <value>
    Given a threshold with operator "<operator>" and value 100
    When the threshold is evaluated with the value <value>
    Then <count> thresholds are triggered

    Examples:
      | operator | value | count |
      | =        | 100   | 1     |
      | =        | 99    | 0     |
      | ==       | 100   | 1     |
      | ==       | 101   | 0     |
      | !=       | 99    | 1     |
      | !=       | 100   | 0     |
      | <>       | 101   | 1     |
      | <>       | 100   | 0     |
      | <=       | 100   | 1     |
      | <=       | 101   | 0     |
      | >=       | 100   | 1     |
      | >=       | 99    | 0     |
      | <        | 99    | 1     |
      | <        | 100   | 0     |
      | >        | 101   | 1     |
      | >        | 100   | 0     |

  Scenario: A metric value can be evaluated by posting it in the request body
    Given a threshold with operator ">=" and value 50
    When the threshold is evaluated by posting the value 75
    Then 1 threshold is triggered

  Scenario: A metric value that misses the threshold triggers nothing when posted
    Given a threshold with operator ">=" and value 50
    When the threshold is evaluated by posting the value 25
    Then 0 thresholds are triggered

  Scenario: Only the thresholds that the metric breaches are returned
    Given a threshold with operator ">" and value 100
    And another threshold with operator ">" and value 1000
    When the threshold is evaluated with the value 500
    Then 1 threshold is triggered
    And the triggered threshold has value 100

  Scenario: Several thresholds sharing a name can be breached at once
    Given a threshold with operator ">" and value 100
    And another threshold with operator "!=" and value 0
    When the threshold is evaluated with the value 500
    Then 2 thresholds are triggered

  Scenario: Every threshold registered under a name is listed regardless of the metric
    Given a threshold with operator ">" and value 100
    And another threshold with operator "<" and value 10
    When the thresholds are looked up by name
    Then 2 thresholds are found

  Scenario: An unknown metric name triggers nothing
    When the threshold is evaluated with the value 100
    Then 0 thresholds are triggered
