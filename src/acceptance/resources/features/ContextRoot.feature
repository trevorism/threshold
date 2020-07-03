Feature: Context Root of this API
  In order to use the Threshold API, it must be available

  Scenario: ContextRoot
    Given the threshold application is alive
    When I navigate to http://threshold.datastore.trevorism.com
    Then the API returns a link to the help page

  Scenario: Ping
    Given the threshold application is alive
    When I navigate to /ping on http://threshold.datastore.trevorism.com
    Then pong is returned, to indicate the service is alive