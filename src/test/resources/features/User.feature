Feature: User tests


  Scenario: get all users by employee
    Given I have valid token for role "employee"
    When I call /users endpoint
    Then I receive the status of 200
    And I get all the users


  Scenario: Posting users with Employee role
    Given I have valid token for role "employee"
    And I have a valid user object with firstname "james" and lastname "ben" and email "james@inholland.nl" and password "test123" and address "1231JK sjkfa, haarlem" and phonenumber "123456789"
    When I make a post request to the users endpoint
    Then I receive the status of 201


  Scenario: Get users from the /api/users  without authentication
    Given I am not logged in
    When I call /users endpoint
    Then I receive the status of 403

  Scenario: Getting users with invalid token
    Given I have invalid token
    When I call /users endpoint
    Then I receive the status of 403

  Scenario: Getting transactions with an expired token
    Given I have expired token
    When I call /users endpoint
    Then I receive the status of 403