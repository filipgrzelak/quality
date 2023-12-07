Feature: New user has been added
  Adding new user

  Scenario: New User
    Given I am logged in as an admin to access users page
    When I navigate to the add new user page
    When I enter user details and save
    Then New user should be visible in the users list and delete user