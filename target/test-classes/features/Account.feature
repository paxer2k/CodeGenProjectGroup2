Feature: Account tests



  Scenario: Retrieving account of a user
    Given I have a valid token for the role "employee"
    When I call the account endpoint by IBAN "NL08INHO0748819013"
    Then I get status code 200


    #works
    Scenario: Retrieving accounts by a customer
      Given I have a valid token for the role "customer"
      When I call the accounts endpoint
      Then I get status code 403
    
    #works
  Scenario: Retrieving all accounts as an employee
    Given I have a valid token for the role "employee"
    When I call the accounts endpoint
    Then I receive all accounts information
    And I get status code 200



  Scenario: Withdrawing money from someone else's bank account as a user
    Given I have a valid token for the role "customer"
    When I try to withdraw with the IBAN "NL08INHO0748819013"
    And I provide the amount of "20.0" and the pincode of "xxxx" for a withdrawal
    Then I get status code 422

  Scenario: Depositing an amount of 0 to my bank account
    Given I have a valid token for the role "employee"
    When I make a deposit to IBAN "NL24INHO0296043871"
    And I provide the amount of "0.0" and the pincode of "xxxx" for a deposit
    Then I get status code 422

  #works
  Scenario: Getting accounts with an expired token
    Given I am provided with an expired token
    When I call the accounts endpoint
    Then I get status code 403


  Scenario: Getting an account of different user as a customer should be forbidden
  Given I have a valid token for the role "customer"
  When I call the account endpoint by IBAN "NL11INHO0177215904"
  Then I get status code 403



#  Scenario: Create an account for a user
#    Given I am provided with an invalid token
#    When I provide a valid userID of "f40a5cee-6479-4615-9cb2-f7e5fbf95bf9" and an accountType of "savings"
#    And I make a post request to accounts
#    Then I get status code 403