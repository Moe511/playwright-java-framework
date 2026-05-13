@ui
Feature: Session Management
  As a Saucedemo user
  I want to log out of the application
  So that my session is terminated and the login page is shown

  @normal
  Scenario: User can log out from the inventory page
    Given I am on the Saucedemo login page
    When I log in with valid credentials
    And I log out
    Then I should be on the login page
    And the page URL should not contain "inventory"

  @normal
  Scenario: Logged-out user cannot access the inventory page directly
    Given I am on the Saucedemo login page
    When I log in with valid credentials
    And I log out
    And I navigate to "https://www.saucedemo.com/inventory.html"
    Then I should be on the login page
