Feature: Threshold validation
  A threshold must have a name, a supported operator and a value before it can be created

  Background:
    Given the threshold application is alive

  Scenario: A threshold without a name is rejected
    When a threshold is created without a name
    Then the request is rejected

  Scenario: A threshold without a value is rejected
    When a threshold is created without a value
    Then the request is rejected

  Scenario: A threshold without an operator is rejected
    When a threshold is created without an operator
    Then the request is rejected

  Scenario Outline: The unsupported operator <operator> is rejected
    When a threshold is created with the operator "<operator>"
    Then the request is rejected

    Examples:
      | operator |
      | =>       |
      | =<       |
      | equals   |
      | ~        |
