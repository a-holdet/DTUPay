Feature: merchant service tests

  Scenario: successful merchant register
    Given A valid Merchant
    And The Merchant is not registered
    When I receive event registerMerchant with merchant
    Then I have sent event registerMerchantSuccess with registered merchant

  Scenario: failed merchant register
    Given An invalid Merchant
    When I receive event registerMerchant with merchant
    Then I have sent event "registerMerchantFail"

#  Scenario: merchant already registered
#    Given A valid Merchant
#    And The Merchant is registered
#    When I receive event registerMerchant with merchant
#    Then I have sent event "registerMerchantFail"

  Scenario: successful get merchant
    Given A valid Merchant
    And The Merchant is registered
    When I receive event getMerchant event with merchantId
    Then I have sent event "getMerchantSuccess"

  Scenario: failed get merchant
    Given A valid Merchant
    And The Merchant is not registered
    When I receive event getMerchant event with merchantId
    Then I have sent event "getMerchantFail"