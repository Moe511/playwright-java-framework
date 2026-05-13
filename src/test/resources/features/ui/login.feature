@ui
Feature: Authentication
  As a Saucedemo user
  I want to log in to the application
  So that I can access the product inventory

  Background:
    Given I am on the Saucedemo login page

  @smoke @blocker
  Scenario: Standard user logs in successfully
    When I log in with valid credentials
    Then the inventory page should load
    And the inventory should show 6 products
    And the page URL should contain "inventory"

  @smoke
  Scenario: Named user logs in with explicit credentials
    When I log in with username "standard_user" and password "secret_sauce"
    Then the inventory page should load

  @critical
  Scenario Outline: Invalid login attempts show the correct error
    When I attempt to log in with username "<username>" and password "<password>"
    Then I should see an error containing "<errorFragment>"
    And the page URL should not contain "inventory"

    Examples:
      | username        | password     | errorFragment |
      | locked_out_user | secret_sauce | locked out    |
      | not_a_real_user | wrong        | do not match  |
      | standard_user   | wrongpass    | do not match  |
