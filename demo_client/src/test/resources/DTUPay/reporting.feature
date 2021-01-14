Feature: Reporting
    Scenario: Successful Reporting
      Given the customer "Stein" "Bagger" with CPR "290276-7777" has a bank account
      And the customer is registered with DTUPay
      And the merchant "Joe" "Exotic" with CPR "207082-0101" has a bank account
      And the merchant is registered with DTUPay
      And the merchant and customer perform a successful payment of 10 kr for a "Gulddame"
      When the merchant requests a report of transactions
      Then the merchant receives a report having a transaction of 10 kr for a "Gulddame" to "Joe" "Exotic" with CPR "207082-0101" using the same token