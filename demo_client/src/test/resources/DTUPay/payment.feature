Feature: Payment
	Scenario: Successful Payment
		Given the customer has a bank account
		And the balance of the customer account is 1000
		And the customer is registering with DTUPay
		And the merchant has a bank account
		And the balance of the merchant account is 2000
		And the merchant is registered with DTUPay
		And the customer requests 5 tokens
		And the customer selects a token
		When the merchant initiates a payment for 10 kr using the selected customer token
		Then the payment is successful
		And the balance of the customer at the bank is 990 kr
		And the balance of the merchant at the bank is 2010 kr

	Scenario: Non-Existing Token
		Given the customer has a bank account
		And the balance of the customer account is 1000
		And the customer is registering with DTUPay
		And the merchant has a bank account
		And the balance of the merchant account is 2000
		And the merchant is registered with DTUPay
		And the customer selects a non-valid token
		When the merchant initiates a payment for 10 kr using the selected customer token
		Then the payment fails
		And the balance of the customer at the bank is 1000 kr
		And the balance of the merchant at the bank is 2000 kr

	Scenario: Used Token
		Given the customer has a bank account
		And the balance of the customer account is 1000
		And the customer is registering with DTUPay
		And the merchant has a bank account
		And the balance of the merchant account is 2000
		And the merchant is registered with DTUPay
		And the customer requests 5 tokens
		And the customer selects a token
		And the merchant initiates a payment for 10 kr using the selected customer token
		When the merchant initiates a payment for 20 kr using the selected customer token
		Then the payment fails
		And the balance of the customer at the bank is 990 kr
		And the balance of the merchant at the bank is 2010 kr

	Scenario: Merchant does not exist
		Given the customer has a bank account
		And the balance of the customer account is 1000
		And the customer is registering with DTUPay
		And the merchant has a bank account
		And the balance of the merchant account is 2000
		And the merchant is not registered with DTUPay
		And the customer requests 5 tokens
		And the customer selects a token
		When the merchant initiates a payment for 20 kr using the selected customer token
		Then the payment fails
		And the balance of the customer at the bank is 1000 kr
		And the balance of the merchant at the bank is 2000 kr

	Scenario: Payment with negative amount
		Given the customer has a bank account
		And the balance of the customer account is 1000
		And the customer is registering with DTUPay
		And the merchant has a bank account
		And the balance of the merchant account is 2000
		And the merchant is registered with DTUPay
		And the customer requests 5 tokens
		And the customer selects a token
		When the merchant initiates a payment for -10 kr using the selected customer token
		Then the payment fails
		And the balance of the customer at the bank is 1000 kr
		And the balance of the merchant at the bank is 2000 kr