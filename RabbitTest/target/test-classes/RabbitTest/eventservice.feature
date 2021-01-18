Feature: EventService feature

  Scenario: Event
    When I receive event "RabbitTest a"
    Then I have sent event "RabbitTest b"
    