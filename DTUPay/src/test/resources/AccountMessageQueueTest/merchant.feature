#/***
#* @Author Michael, s153587
#*/
Feature: handle merchant from account microservice

  Scenario: register merchant successful
    Given a merchant
    And merchant is not registered
    When I want to register merchant
    Then I have sent event "registerMerchant"
    When I receive event "registerMerchantSuccess"
    Then service is successful

  Scenario: get merchant successful
    Given a merchant
    And merchant is registered
    When I want to get merchant
    Then I have sent event "getMerchant"
    When I receive event "getMerchantSuccess"
   Then service is successful