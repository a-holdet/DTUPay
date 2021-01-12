package behaviourtests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.EventSender;
import service2.businesslogic.Service2;

public class Service2Steps {
	Service2 s;
	Event event;
	CompletableFuture<Boolean> result = new CompletableFuture<>();
	
	public Service2Steps() {
		s = new Service2(new EventSender() {

			@Override
			public void sendEvent(Event ev) throws Exception {
				event = ev;
			}});
	}
	@When("I do something")
	public void iDoSomething() {
		new Thread(() -> {try {
			result.complete(s.doSomething());
		} catch (Exception e) {
			throw new Error(e);
		}}).start();
	}
	
	@Then("I have sent event {string}")
	public void iHaveSentEvent(String string) {
		assertEquals(string,event.getEventType());
	}
	
	@When("I receive event {string}")
	public void iReceiveEvent(String string) {
	    s.receiveEvent(new Event(string));
	}
	
	@Then("do something is successful")
	public void doSomethingIsSuccessful() {
		assertTrue(result.join());
	}
	
}

