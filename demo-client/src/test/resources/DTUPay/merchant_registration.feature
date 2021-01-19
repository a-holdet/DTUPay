#/***
#* @Author Martin Hemmingsen, s141887
#*/



Feature: Merchant Registration
  Scenario: Successful Registration
    Given the merchant has a bank account
    When the merchant is registering with DTUPay
    Then the merchant registration is successful
  Scenario: No Bank Account
    Given the merchant has no bank account
    When the merchant is registering with DTUPay
    Then the merchant registration is not successful
    And the error message is "Merchant must have a bank account to be created in DTUPay"