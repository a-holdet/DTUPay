Feature: Reporting
    Scenario: Successful Merchant Reporting
      Given the customer has a bank account
      And the customer is registering with DTUPay
      And the merchant has a bank account
      And the merchant is registering with DTUPay
      And the merchant and customer perform a successful payment of 10 kr for a "Gulddame"
      When the merchant requests a report of transactions
      Then the merchant receives a report having a transaction of 10 kr for a "Gulddame" to the merchant using the same token

    Scenario: Merchant can only see own report
      Given the customer has a bank account
      And the customer is registering with DTUPay
      And the merchant has a bank account
      And the merchant is registering with DTUPay
      And another merchant has a bank account
      And the other merchant is registering with DTUPay
      And the merchant and customer perform a successful payment of 10 kr for a "Gulddame"
      And the other merchant and customer perform a successful payment of 20 kr for a "Tuborg"
      When the merchant requests a report of transactions
      Then he does not see the other merchants report

    Scenario: DTUPay can access all reports
      Given the customer has a bank account
      And the customer is registering with DTUPay
      And the merchant has a bank account
      And the merchant is registering with DTUPay
      And another merchant has a bank account
      And the other merchant is registering with DTUPay
      And the merchant and customer perform a successful payment of 10 kr for a "Gulddame"
      And the other merchant and customer perform a successful payment of 20 kr for a "Tuborg"
      When DTUPay requests a report of transactions
      Then DTUPay receives a report including both transactions
      And the customer is registering with DTUPay
      And the merchant has a bank account
      And the merchant is registering with DTUPay
      And another merchant has a bank account
      And the other merchant is registering with DTUPay
      And the merchant and customer perform a successful payment of 10 kr for a "Gulddame"
      And the other merchant and customer perform a successful payment of 20 kr for a "Tuborg"
      When DTUPay requests a report of transactions
      Then DTUPay receives a report including both transactions

    Scenario: Successful Customer Reporting
      Given the customer has a bank account
      And the customer is registering with DTUPay
      And the merchant has a bank account
      And the merchant is registering with DTUPay
      And the merchant and customer perform a successful payment of 10 kr for a "Gulddame"
      When the customer requests a report of transactions
      Then the customer receives a report having a transaction of 10 kr for a "Gulddame" to the merchant using the same token
