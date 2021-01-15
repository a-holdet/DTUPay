package RabbitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

public class EventServiceSteps {
	EventService s;
	Event event;

	public EventServiceSteps() {
		s = new EventService(new IEventSender() {

			@Override
			public void sendEvent(Event ev) throws Exception {
				event = ev;
			}
		});
	}

	@When("I receive event {string}")
	public void iReceiveEvent(String string) throws Exception {
		s.receiveEvent(new Event(string));
	}

	@Then("I have sent event {string}")
	public void iHaveSentEvent(String string) {
		assertEquals(string, event.getEventType());
	}
}
