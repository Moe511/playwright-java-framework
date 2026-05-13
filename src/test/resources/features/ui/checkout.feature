@ui
Feature: Checkout
  As a logged-in Saucedemo user with items in my cart
  I want to complete a purchase
  So that my order is confirmed

  Background:
    Given I am logged in as a standard user
    And I have added "Sauce Labs Backpack" to the cart
    And I have proceeded to checkout

  @smoke @blocker
  Scenario: Successful end-to-end checkout
    When I fill in checkout details with first name "Mohamed", last name "Ahmed", postal code "07002"
    And I continue and finish the order
    Then I should see the order confirmation

  @normal
  Scenario: Missing postal code blocks checkout
    When I fill in checkout details with first name "Mohamed", last name "Ahmed", postal code ""
    And I click continue
    Then I should see a checkout error containing "Postal Code"
