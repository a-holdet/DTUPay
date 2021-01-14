Feature: TokenGeneration
	Scenario: Successful Token Generation
		Given the customer has a bank account
		And the customer is registered with DTUPay
		And the customer has 0 tokens
		When the customer requests 5 tokens
		Then the customer has 5 tokens

	Scenario: Customer has no Bank Account
		Given the customer has no bank account
		When the customer requests 5 tokens
		Then the token granting is not successful
		And the received error message is "Customer must have a customer id to request tokens"

	Scenario: Customer has multiple tokens and requests more tokens
		Given the customer has a bank account
		And the customer is registered with DTUPay
		And the customer has 0 tokens
		And the customer requests 2 tokens
		And the customer has 2 tokens
		When the customer requests 2 tokens
		Then the token granting is denied
		And the error message is "Customer cannot request more tokens"

	Scenario: Customer requests too many tokens
		Given the customer has a bank account
		And the customer is registered with DTUPay
		And the customer has 0 tokens
		And the customer requests 1 tokens
		And the customer has 1 tokens
		When the customer requests 6 tokens
		Then the token granting is denied
		And the error message is "Customer requested too many tokens"