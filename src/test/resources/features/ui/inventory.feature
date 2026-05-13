@ui
Feature: Inventory Management
  As a logged-in Saucedemo user
  I want to manage items and sort the product list
  So that I can find and control what is in my cart

  Background:
    Given I am logged in as a standard user

  @normal
  Scenario: Remove an item from the inventory page
    When I add "Sauce Labs Backpack" to the cart
    Then the cart badge should show 1
    When I remove "Sauce Labs Backpack" from the inventory
    Then the cart badge should show 0

  @normal
  Scenario: Remove an item from the cart page
    When I add "Sauce Labs Backpack" to the cart
    And I open the cart
    Then the cart should contain 1 items
    When I remove "Sauce Labs Backpack" from the cart
    Then the cart should contain 0 items

  @normal
  Scenario Outline: Sorting products updates the displayed order
    When I sort products by "<sortOption>"
    Then the first product name should be "<expectedFirst>"

    Examples:
      | sortOption           | expectedFirst                    |
      | Name (A to Z)        | Sauce Labs Backpack               |
      | Name (Z to A)        | Test.allTheThings() T-Shirt (Red) |
      | Price (low to high)  | Sauce Labs Onesie                 |
      | Price (high to low)  | Sauce Labs Fleece Jacket          |

  @normal
  Scenario: Sort by name ascending produces alphabetical order
    When I sort products by "Name (A to Z)"
    Then product names should be in ascending order

  @normal
  Scenario: Sort by price ascending produces lowest-first order
    When I sort products by "Price (low to high)"
    Then product prices should be in ascending order
