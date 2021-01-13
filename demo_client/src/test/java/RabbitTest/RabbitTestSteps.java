package RabbitTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import RabbitTest.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RabbitTestSteps {
	RabbitTestService service = new RabbitTestService();
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

