#/***
# * @Author Benjamin Wrist Lam, s153486
# */
Feature: Customer Registration
    Scenario: Successful Registration
       Given the customer has a bank account
        When the customer is registering with DTUPay
        Then the customer registration is successful
    Scenario: No Bank Account
        Given the customer has no bank account
        When the customer is registering with DTUPay
        Then the customer registration is not successful
        And the error message is "Customer must have a bank account to be created in DTUPay"