Feature: Validating api response

  Scenario: Validating JSON response

    When a user queries for postcode
    Then it should produce expected JSON format

  Scenario: Validate against database

    When user queries for random "NR330AU"
    Then ensure that it matches the database