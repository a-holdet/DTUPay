Feature: Reporting time interval
  Scenario: Successful customer reporting with a time interval
    Given a customer that is registered
    And a transaction is made on date "2021-01-05T11:30:30" within the time interval
    And a transaction is made on date "2021-01-10T11:30:30" outside the time interval
    When when the requester requests a report in the interval between "2021-01-04T11:30:30" and "2021-01-08T11:30:30"
    Then the customer receives only the transaction from the interval


  Scenario: Successful customer reporting with a time interval - both payments
    Given a customer that is registered
    And a transaction is made on date "2021-01-05T11:30:30" within the time interval
    And a transaction is made on date "2021-01-10T11:30:30" within the time interval
    When when the requester requests a report in the interval between "2021-01-01T11:30:30" and "2021-01-12T11:30:30"
    Then the customer receives only the transaction from the interval

  Scenario: Successful customer reporting with a time interval - no payments
    Given a customer that is registered
    And a transaction is made on date "2021-01-05T11:30:30" outside the time interval
    And a transaction is made on date "2021-01-10T11:30:30" outside the time interval
    When when the requester requests a report in the interval between "2021-01-08T11:30:30" and "2021-01-09T11:30:30"
    Then the customer receives only the transaction from the interval

  Scenario: Successful customer reporting with no time interval
    Given a customer that is registered
    And a transaction is made on date "2021-01-05T11:30:30" within the time interval
    And a transaction is made on date "2021-01-10T11:30:30" within the time interval
    When when the requester requests a report
    Then the customer receives all the transactions