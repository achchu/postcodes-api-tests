Feature: Search for postcode

  Scenario Outline: Test all possible cases

    When user requests end point with "<parameter>"
    Then user should be able to see response with "<status code>"
    Examples:
    | parameter | status code|
    |           |  400 |
    | SW17E     |  404 |
    | NR330AU   |  200 |

  Scenario: User queries for postcode

    When user query for postcode with "NR"
    Then user should receive list of postcodes starting with above strings

  Scenario: Retrieving postcode information via bulk lookup post codes

    When user requests bulk lookup postcodes
    | NR330AU   |
    | CR05QT    |
    | NR338JP   |
    Then the correct response should be given
