Feature: EventService feature

  Scenario: Event
    When I receive event "RabbitTest a"
    Then I have sent event "RabbitTest b"

  Scenario: successful merchant register
    Given a valid Merchant
    When I receive event registerMerchant with merchant
    Then I have sent event registerMerchantSuccess with registered merchant
    