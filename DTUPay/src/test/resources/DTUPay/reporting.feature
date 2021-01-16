#Feature Reporting Timestamps
#  Scenario: Successful Report Generation With Time Filtering
#    Given a transaction between a consumer and merchant at time "02/01/2021"
#    And another transaction between them at time "/04/01/2021"
#    When the merchant requests a report in the interval "03/01/2021" to "05/01/2021"
#    Then the report only includes the transaction at time "/04/01/2021"