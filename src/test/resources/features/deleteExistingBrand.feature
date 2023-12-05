
Feature: Delete existing test brand

  Scenario: Delete exising test brand
    Given I am logged in as an admin
    And Add example brand to testing deleting brand
    When Delete existing test brand
    Then Should not find deleted object