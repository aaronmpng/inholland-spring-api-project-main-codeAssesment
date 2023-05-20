Feature: Authenticate

  Scenario: Post request to /authenticate will result in bearer token
    Given I have a valid user object
    When I call the authenticate endpoint
    Then I receive a status of 200
    And I get a token

  Scenario: Post request to /authenticate with invalid user object
    Given I have an invalid user object
    When I call the authenticate endpoint
    Then I receive a status of 400