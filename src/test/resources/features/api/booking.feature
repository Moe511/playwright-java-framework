@api
Feature: Booking CRUD
  As a restful-booker API consumer
  I want to manage hotel bookings
  So that I can create, read, update, and delete reservations

  @blocker
  Scenario: Create a booking returns a booking ID
    When I create a booking with default data
    Then the response should include a positive booking ID
    And the booking firstname should be "Mohamed"

  @critical
  Scenario: Read a created booking
    Given I have created a booking with default data
    When I retrieve the booking by its ID
    Then the booking firstname should be "Mohamed"
    And the booking lastname should be "Ahmed"
    And the booking total price should be 250

  @critical
  Scenario: Update a booking with a valid token
    Given I am authenticated with the booking API
    And I have created a booking with default data
    When I update the booking lastname to "Updated" and total price to 999
    Then the booking lastname should be "Updated"
    And the booking total price should be 999

  @critical
  Scenario: Delete a booking removes it
    Given I am authenticated with the booking API
    And I have created a booking with default data
    When I delete the booking
    Then the delete response should be 200 or 201
    And retrieving the booking by ID should return 404

  @normal
  Scenario: Create a booking with custom check-in and check-out dates
    When I create a booking with check-in "2026-12-24" and check-out "2026-12-26"
    Then the response should include a positive booking ID
    And the booking check-in date should be "2026-12-24"
    And the booking check-out date should be "2026-12-26"

  @normal
  Scenario Outline: Creating bookings with different guest data
    When I create a booking with firstname "<firstname>", lastname "<lastname>", and total price <price>
    Then the response should include a positive booking ID
    And the booking firstname should be "<firstname>"
    And the booking lastname should be "<lastname>"

    Examples:
      | firstname | lastname  | price |
      | Alice     | Smith     | 100   |
      | Bob       | Jones     | 350   |
      | Carol     | Williams  | 99    |
