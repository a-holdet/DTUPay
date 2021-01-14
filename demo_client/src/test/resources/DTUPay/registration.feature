Feature: Registration
    Scenario: Successful Payment
        Given the customer has no bank account
        When the customer is registered with DTUPay
        Then the registration is not successful
        And the error message is "Customer must have an account id to be created in DTUPay"