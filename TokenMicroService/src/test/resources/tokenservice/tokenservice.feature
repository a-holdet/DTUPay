Feature: TokenService
  Scenario: Successful create tokens for customer
    Given a valid customer
    When I receive event createTokensForCustomer with 5 tokens
    Then I sent customerExists
    When I receive event customerExistsSuccess
    Then I have sent event createTokensForCustomerSuccess with 5 tokens