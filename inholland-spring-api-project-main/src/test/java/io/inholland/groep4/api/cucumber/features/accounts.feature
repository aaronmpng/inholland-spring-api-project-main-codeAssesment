Feature: Accounts

  Scenario: Getting all accounts as employee
    Given I have a valid "employee" token
    When I call the accounts endpoint
    Then the response is a 200 statuscode
    Then the result is a list of 9 accounts

  Scenario: Getting your own account
    Given I have a valid "user" token
    When I call the accounts endpoint
    Then the response is a 200 statuscode
    Then the result is a list of 1 accounts

  Scenario: Getting a specific account without permission
    Given I have a valid "user" token
    When I call the accounts endpoint with accountid 1
    Then the response is a 403 statuscode

  Scenario: Getting accounts with an invalid token
    Given I have an "invalid" bearertoken
    When I call the accounts endpoint
    Then the response is a 403 statuscode

  Scenario: Getting accounts with an expired token
    Given I have an "expired" bearertoken
    When I call the accounts endpoint
    Then the response is a 403 statuscode

  Scenario: Posting an account with employee role
    Given I have a valid "user" token
    And I have a valid account object with "savings" as type
    When I make a post request to the accounts endpoint
    Then the response is a 403 statuscode