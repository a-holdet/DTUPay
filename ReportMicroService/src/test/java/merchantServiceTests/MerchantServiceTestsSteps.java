package merchantServiceTests;

public class MerchantServiceTestsSteps {
	/*ReportServicePortAdapter s;
	Event event;
	Merchant merchant;

	public MerchantServiceTestsSteps() {
		s = new ReportServicePortAdapter(new IEventSender() {
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

	@Given("A valid Merchant")
	public void aValidMerchant() {
		merchant = new Merchant();
		merchant.accountId = "123123123";
		merchant.cprNumber = "test";
		merchant.firstName = "test";
		merchant.lastName = "test";
		assertNull(merchant.id);
	}

	@When("I receive event registerMerchant with merchant")
	public void iReceiveEventRegisterMerchantWithMerchant() throws Exception {
		s.receiveEvent(new Event("registerMerchant", new Object[] {merchant}));
	}

	@Then("I have sent event registerMerchantSuccess with registered merchantId")
	public void iHaveSentEventRegisterMerchantSuccessWithRegisteredMerchantId() {
		String type = event.getEventType();
		assertEquals("registerMerchantSuccess", type);

		String merchantId = event.getArgument(0, String.class);

		assertNotNull(merchantId);
	}

	@Given("An invalid Merchant")
	public void anInvalidMerchant() {
		merchant = new Merchant();
	}

	@And("The Merchant is registered")
	public void theMerchantIsRegistered() throws Exception {
		s.registerMerchant(merchant, null);
		merchant.id = event.getArgument(0, String.class);
		event = null; //overwrite sent event
	}

	@And("The Merchant is not registered")
	public void theMerchantIsNotRegistered() {
		merchant.id = UUID.randomUUID().toString();
	}

	@When("I receive event getMerchant event with merchantId")
	public void iReceiveEventGetMerchantEventWithMerchantId() throws Exception {
		s.receiveEvent(new Event("getMerchant", new Object[] {merchant.id}));
	}*/


}
