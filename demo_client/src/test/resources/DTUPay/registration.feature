Feature: Registration
    Scenario: Successful Payment
        Given the customer "Stein" "Bagger" with CPR "290276-1236" does not have a bank account
        When the customer is registering with DTUPay
        Then the registration is not successful
        And the error message is "Customer must have an account id to be created in DTUPay"