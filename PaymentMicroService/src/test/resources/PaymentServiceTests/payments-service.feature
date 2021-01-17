Feature: Payments Service Test
  Scenario: Successful payment
    Given a valid merchant, customer and token
    When I receive a valid payment
    Then I send back a valid response