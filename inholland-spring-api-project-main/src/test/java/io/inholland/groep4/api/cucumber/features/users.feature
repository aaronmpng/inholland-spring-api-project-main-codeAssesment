Feature: Users

  Scenario: Getting all users
    Given I have a valid token for role "employee"
    When I call the user endpoint
    Then the result is a status of 200
    Then the result is a list of users of size 4

  Scenario: Getting your own user
    Given I have a valid token for role "user"
    When I call the user endpoint
    Then the result is a status of 200
    Then the result is a list of users of size 1

  Scenario: Getting a specific user without permission
    Given I have a valid token for role "user"
    When I call the user endpoint with userid 2
    Then the result is a status of 403

  Scenario: Getting users with an invalid token
    Given I have an "invalid" token
    When I call the user endpoint
    Then the result is a status of 403

  Scenario: Getting users with an expired token
    Given I have an "expired" token
    When I call the user endpoint
    Then the result is a status of 403

  Scenario: Posting user with user role
    Given I have a valid token for role "user"
    And I have a valid user object with username "johndoe" and password "test"
    When I make a post request to the user endpoint
    Then the result is a status of 403

  Scenario: Posting user with employee role
    Given I have a valid token for role "employee"
    And I have a valid user object with username "johndoe" and password "test"
    When I make a post request to the user endpoint
    Then the result is a status of 201
    And I validate the user object has an id greater than 10

  Scenario: Posting invalid user with employee role should fail
    Given I have a valid token for role "employee"
    And I have an invalid user object with username "johndoe"
    When I make a post request to the user endpoint
    Then the result is a status of 400