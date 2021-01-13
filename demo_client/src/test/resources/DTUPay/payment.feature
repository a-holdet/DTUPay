Feature: Payment
	Scenario: Successful Payment
		Given the customer "Stein" "Bagger" with CPR "290276-7777" has a bank account
		And the balance of that account is 1000
		And the customer is registered with DTUPay
		And the merchant "Joe" "Exotic" with CPR "207082-0101" has a bank account
		And the balance of that account is 2000
		And the merchant is registered with DTUPay
		And the customer requests 5 tokens
		And the customer selects a token
		When the merchant initiates a payment for 10 kr using the selected customer token
		Then the payment is successful
		And the balance of the customer at the bank is 990 kr
		And the balance of the merchant at the bank is 2010 kr