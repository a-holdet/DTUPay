Feature: EventService feature

  Scenario: Event
    When I receive event "TokenTest a"
    Then I have sent event "TokenTest b"
