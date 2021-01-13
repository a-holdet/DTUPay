package RabbitTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Steps {
	Service service = new Service();
	private boolean result;
	
	@When("I do something")
	public void iDoSomething() {
		result = service.doSomething();
	}
	
	@Then("do something succeeds")
	public void doSomethingSucceeds() {
		assertTrue(result);
	}
}

