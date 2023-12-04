Feature: New category have been added

  Scenario: New Category
    Given I am logged in as an admin
    When I navigate to the add new category page
    When I enter category details and save
    Then The new category should be visible in the categories list