Feature: Transaction tests

  Scenario: get all transactions for different user while logged in as customer
    Given I have a valid token for role "customer"
    When I call the transactions endpoint
    Then the result is a status of 405


  Scenario: get all transactions for different user while logged as employee
    Given I have a valid token for role "employee"
    When I call the transactions endpoint
    Then the result is a status of 200
    And I get all the transactions

  Scenario: get all transactions for user who owns following iban
    Given I have a valid token for role "customer"
    When I call the transactions endpoint
    Then the result is a status of 200
    And I get all the transactions


  Scenario: Getting transactions with invalid token
    Given I have an invalid token
    When I call the transactions endpoint
    Then the result is a status of 403

  Scenario: Getting transactions with an expired token
    Given I have an expired token
    When I call the transactions endpoint
    Then the result is a status of 403

  Scenario: Posting transaction with customer role
    Given I have a valid token for role "customer"
    And I have a valid transaction object with fromIBAN "NL30INHO0065055626" and toIBAN "NL22INHO0306625928" and amount 10 and userID "1f575c19-faae-4ceb-8e04-1f67af60a9c0" and pincode "xxxx"
    When I make a post request to the transactions endpoint
    Then the result is a status of 201

  Scenario: Posting transaction with Employee role
    Given I have a valid token for role "employee"
    And I have a valid transaction object with fromIBAN "NL30INHO0065055626" and toIBAN "NL22INHO0306625928" and amount 10 and userID "1f575c19-faae-4ceb-8e04-1f67af60a9c0" and pincode "xxxx"
    When I make a post request to the transactions endpoint
    Then the result is a status of 201

  Scenario: Getting transaction without token
    Given I have no token
    When I call the transactions endpoint
    Then the result is a status of 403

