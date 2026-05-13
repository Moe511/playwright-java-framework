@api
Feature: API Authentication
  As a restful-booker API consumer
  I want to authenticate with valid credentials
  So that I can use protected endpoints

  @blocker
  Scenario: Valid credentials return an auth token
    When I authenticate with valid API credentials
    Then I should receive a non-empty auth token
