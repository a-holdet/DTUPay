package PaymentServiceTests;

import customerservice.CustomerInMemoryRepository;
import customerservice.LocalCustomerService;
import io.cucumber.java.an.E;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import merchantservice.LocalMerchantService;
import merchantservice.Merchant;
import merchantservice.MerchantInMemoryRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messagequeue.MessageQueueConnector;
import messaging.rmq.event.EventExchange;
import messaging.rmq.event.EventExchangeFactory;
import messaging.rmq.event.interfaces.IEventSender;
import messaging.rmq.event.objects.Event;
import messaging.rmq.event.objects.EventType;

import java.util.UUID;

import static org.junit.Assert.*;

/***
 * @Author Michael Davidsen Kirkegaard, s153587
 */


public class MerchantServiceTestsSteps {
	MessageQueueConnector service;
	Event event;
	Merchant merchant;

	public MerchantServiceTestsSteps() {
		IEventSender sender = new IEventSender() {
			@Override
			public void sendEvent(Event ev) {
				event = ev;
			}
        };
		service = new MessageQueueConnector(
				new LocalMerchantService(
						new MerchantInMemoryRepository()
				),
				new LocalCustomerService(
						new CustomerInMemoryRepository()
				),
                sender
		);
	}

	@When("I receive event {string}")
	public void iReceiveEvent(String string) {
		service.receiveEvent(new Event(string));
	}

	@Then("I have sent event {string}")
	public void iHaveSentEvent(String string) {
		assertEquals(string, event.getEventType());
	}

	@Given("A valid Merchant")
	public void aValidMerchant() {
		merchant = new Merchant();
		merchant.accountId = UUID.randomUUID().toString();
		assertNull(merchant.id);
	}

	@When("I receive event registerMerchant with merchant")
	public void iReceiveEventRegisterMerchantWithMerchant() throws Exception {
		service.receiveEvent(new Event("registerMerchant", new Object[] {merchant}));
	}

	@And("event contains merchantId")
	public void eventContainsMerchantId() {
		merchant.id = event.getArgument(0, String.class);
		assertNotNull(merchant.id);
	}

	@Given("An invalid Merchant")
	public void anInvalidMerchant() {
		merchant = new Merchant();
	}

	@And("The Merchant is registered")
	public void theMerchantIsRegistered() {
		service.registerMerchant(merchant, null);
		merchant.id = event.getArgument(0, String.class);
		event = null; //overwrite sent event
	}

	@And("The Merchant is not registered")
	public void theMerchantIsNotRegistered() {
		merchant.id = UUID.randomUUID().toString();
	}

	@When("I receive event getMerchant event with merchantId")
	public void iReceiveEventGetMerchantEventWithMerchantId() {
		service.receiveEvent(new Event("getMerchant", new Object[] {merchant.id}));
	}
}
