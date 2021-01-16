package RabbitTest;

import io.cucumber.java.en.Given;
import merchantservice.Merchant;
import messagequeue.EventService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;

import static org.junit.Assert.*;

public class EventServiceSteps {
	EventService s;
	Event event;
	Merchant validMerchant;

	public EventServiceSteps() {
		s = new EventService(new IEventSender() {
			@Override
			public void sendEvent(Event ev) {
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

	@Given("a valid Merchant")
	public void aValidMerchant() {
		validMerchant = new Merchant();
		validMerchant.accountId = "123123123";
		validMerchant.cprNumber = "test";
		validMerchant.firstName = "test";
		validMerchant.lastName = "test";
		assertNull(validMerchant.id);
	}

	@When("I receive event registerMerchant with merchant")
	public void iReceiveEventRegisterMerchantWithMerchant() throws Exception {
		s.receiveEvent(new Event("registerMerchant", new Object[] {validMerchant}));
	}

	@Then("I have sent event registerMerchantSuccess with registered merchant")
	public void iHaveSentEventRegisterMerchantSuccessWithRegisteredMerchant() {
		String type = event.getEventType();
		assertEquals("registerMerchantSuccess", type);

		Object[] arguments = event.getArguments();
		Merchant merchant = (Merchant) arguments[0];
		assertNotNull(merchant.id);
	}
}
