package RabbitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.EventSender;
import RabbitTest.Service1;

public class Sevice1Steps {
	Service1 s;
	Event event;
	
	public Sevice1Steps() {
		s = new Service1(new EventSender() {

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
		assertEquals(string,event.getEventType());
	}
}

