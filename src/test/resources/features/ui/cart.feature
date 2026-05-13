@ui
Feature: Shopping Cart
  As a logged-in Saucedemo user
  I want to add products to my cart
  So that I can review them before purchasing

  Background:
    Given I am logged in as a standard user

  @normal
  Scenario: Adding one item updates the cart badge
    When I add "Sauce Labs Backpack" to the cart
    Then the cart badge should show 1

  @normal
  Scenario: Multiple items appear in the cart
    When I add the following items to the cart:
      | Sauce Labs Backpack     |
      | Sauce Labs Bike Light   |
      | Sauce Labs Bolt T-Shirt |
    And I open the cart
    Then the cart should contain 3 items
    And the cart should include "Sauce Labs Backpack"
    And the cart should include "Sauce Labs Bike Light"
    And the cart should include "Sauce Labs Bolt T-Shirt"
